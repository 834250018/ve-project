package cn.ve.thirdgateway.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.ve.base.pojo.VeBaseException;
import cn.ve.thirdgateway.dal.entity.WxUnionidRelation;
import cn.ve.thirdgateway.dal.mapper.WxUnionidRelationMapper;
import cn.ve.thirdgateway.pojo.*;
import cn.ve.thirdgateway.service.WechatService;
import cn.ve.thirdgateway.util.WechatUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ve
 * @date 2022/2/23
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {
    @Resource
    private WxUnionidRelationMapper wxUnionidRelationMapper;
    @Value("${wechat.official-account.appid}")
    private String officialAccountAppid;
    @Value("${wechat.official-account.secret}")
    private String officialAccountSecret;
    @Value("${wechat.official-account.message.template-id}")
    private String templateId;
    @Value("${wechat.url.access_token}")
    private String accessTokenUrl;
    @Value("${wechat.callback.token}")
    private String wechatOfficialAccountCallbackToken;
    @Value("${wechat.callback.encoding-AES-Key}")
    private String wechatOfficialAccountAESKey;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void officialAccountMsg(OfficialAccountMsgParam param) {
        WxUnionidRelation wxUnionidRelation = wxUnionidRelationMapper.selectOne(
            new LambdaQueryWrapper<WxUnionidRelation>().eq(WxUnionidRelation::getUnionid, param.getUnionid()));
        if (wxUnionidRelation == null || wxUnionidRelation.getStatus() == 0) {
            return;
        }
        String wxMessageTemplateUrl =
            "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getAccessToken(
                officialAccountAppid, officialAccountSecret);

        RestTemplate restTemplate = new RestTemplate();
        WxMessage request = new WxMessage();
        request.setTemplate_id(templateId);
        // ??????????????????
        Map<String, DataBody> data = new HashMap<>();
        // ????????????: first
        // ??????: keyword1
        // ??????: keyword2
        // ??????: keyword3
        // ????????????: remark
        data.put("first", new DataBody(param.getTitle(), "#173177"));
        data.put("keyword1", new DataBody(param.getName(), "#173177"));
        data.put("keyword2", new DataBody(param.getTime(), "#173177"));
        data.put("keyword3", new DataBody(param.getStatus(), "#173177"));
        data.put("remark", new DataBody(param.getRemark(), "#173177"));
        request.setData(data);
        request.setMiniprogram(new Miniprogram());
        request.getMiniprogram().setAppid(officialAccountAppid);
        request.getMiniprogram().setPagepath("/src/pages/user/index");
        request.setTouser(wxUnionidRelation.getOpenid());
        try {
            restTemplate.postForEntity(wxMessageTemplateUrl, request, Object.class);
        } catch (Exception e) {
            log.error("???????????????????????????:{}", e.getMessage(), e);
        }
    }

    @Override
    public String getAccessToken(String appid, String secret) {
        String accessToken = (String)redisTemplate.opsForValue().get(appid);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }

        String url = MessageFormat.format(accessTokenUrl, appid, secret);
        RestTemplate restTemplate = new RestTemplate();
        // todo ??????????????????client,???????????????
        ResponseEntity<Map> objectResponseEntity = restTemplate.postForEntity(url, null, Map.class);
        if (objectResponseEntity.getStatusCode() != org.springframework.http.HttpStatus.OK) {
            log.error("????????????????????????_{}", objectResponseEntity.getStatusCode());
            throw new VeBaseException("????????????????????????");
        }
        Map map = objectResponseEntity.getBody();
        if (ObjectUtil.isNotEmpty(map) && ObjectUtil.isNotEmpty(map.get("access_token"))) {
            accessToken = (String)map.get("access_token");
            redisTemplate.opsForValue().set(appid, accessToken, 7150, TimeUnit.SECONDS);
        }
        return accessToken;
    }

    @Override
    public void loadUnionId() {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = getAccessToken(officialAccountAppid, officialAccountSecret);
        String s = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=";
        String nextOpenId = "";
        for (int i = 10; i > 0; i--) {
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(s + nextOpenId, Map.class);
            if (forEntity.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                return;
            }
            Map body = forEntity.getBody();
            Integer total = (Integer)body.get("total");
            Integer count = (Integer)body.get("count");
            Map data = (Map)body.get("data");
            List<String> openids = (List<String>)data.get("openid");
            List<WxUnionidRelation> wxUnionidRelations = wxUnionidRelationMapper
                .selectList(new LambdaQueryWrapper<WxUnionidRelation>().in(WxUnionidRelation::getOpenid, openids));
            List<String> collect =
                wxUnionidRelations.stream().map(WxUnionidRelation::getOpenid).collect(Collectors.toList());
            for (String openid : openids) {
                if (collect.contains(openid)) {
                    continue;
                }

                ResponseEntity<Map> queryUserEntity = new RestTemplate().getForEntity(
                    "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid,
                    Map.class);
                if (queryUserEntity.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                    throw new VeBaseException(400, "????????????????????????");
                }
                // ?????????????????????????????????????????????0???????????????????????????????????????????????????????????????????????????
                Integer subscribe = (Integer)queryUserEntity.getBody().get("subscribe");
                if (subscribe == 0) {
                    continue;
                }

                // ??????unionid?????????
                WxUnionidRelation insert = new WxUnionidRelation();
                insert.setOpenid(openid);
                insert.setUnionid((String)queryUserEntity.getBody().get("unionid"));
                insert.setStatus(1);
                wxUnionidRelationMapper.insert(insert);
            }

            if (count >= 10000) {
                break;
            }
            nextOpenId = (String)body.get("next_openid");
        }
    }

    @Override
    public Long wechatCallback(UriParam uriParam, String xmlMsg) {
        // 1.???token???timestamp???nonce?????????????????????????????????
        String collect =
            Stream.of(wechatOfficialAccountCallbackToken, uriParam.getTimestamp(), uriParam.getNonce()).sorted()
                .collect(Collectors.joining());
        // 2.??????????????????????????????????????????????????????sha1??????
        String sha1 = DigestUtil.sha1Hex(collect);
        // 3.??????????????????????????????????????????signature???????????????????????????????????????
        if (!sha1.equals(uriParam.getSignature())) {
            log.error("sha1???????????????");
            return null;
        }
        if (uriParam.getEchostr() != null) {
            return uriParam.getEchostr();
        }
        // ??????xml
        Map<String, String> map = WxPayKit.xmlToMap(xmlMsg);
        String encrypt = map.get("Encrypt");

        byte[] msgWithRandomBytes;
        try {
            byte[] secretKeyBytes = cn.hutool.core.codec.Base64.decode(wechatOfficialAccountAESKey + "=");
            msgWithRandomBytes =
                WechatUtil.decrypt(Base64.decode(encrypt), secretKeyBytes, Arrays.copyOfRange(secretKeyBytes, 0, 16));
        } catch (Exception e) {
            log.error("????????????: {}", e.getMessage(), e);
            return null;
        }
        String msgWithRandomString = new String(msgWithRandomBytes, StandardCharsets.UTF_8);
        if (!msgWithRandomString.endsWith(officialAccountAppid)) {
            log.error("officialAccountAppid???????????????");
            return null;
        }
        // ????????????
        msgWithRandomString = msgWithRandomString.replaceAll(officialAccountAppid, "");
        byte[] msgWithPrefixBytes = msgWithRandomString.getBytes(StandardCharsets.UTF_8);
        // ????????????
        byte[] msgBytes = new byte[msgWithPrefixBytes.length - 20];
        System.arraycopy(msgWithPrefixBytes, 20, msgBytes, 0, msgBytes.length);
        String xml = new String(msgBytes);
        Map<String, String> decryptData = WxPayKit.xmlToMap(xml);
        // ????????????????????????OpenID???
        String fromUserName = decryptData.get("FromUserName");
        // 	???????????????subscribe(??????)???unsubscribe(????????????)
        String event = decryptData.get("Event");
        if ("subscribe".equals(event)) {
            // ??????unionid
            ResponseEntity<Map> forEntity = new RestTemplate().getForEntity(
                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAccessToken(officialAccountAppid,
                    officialAccountSecret) + "&openid=" + fromUserName, Map.class);
            if (forEntity.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                throw new VeBaseException(400, "????????????????????????");
            }
            Object unionid = forEntity.getBody().get("unionid");
            if (unionid != null) {
                WxUnionidRelation wxUnionidRelation = wxUnionidRelationMapper.selectOne(
                    new LambdaQueryWrapper<WxUnionidRelation>()
                        .eq(WxUnionidRelation::getUnionid, String.valueOf(unionid)));
                if (wxUnionidRelation == null) {
                    WxUnionidRelation insert = new WxUnionidRelation();
                    insert.setOpenid(fromUserName);
                    insert.setUnionid(String.valueOf(unionid));
                    insert.setStatus(1);
                    wxUnionidRelationMapper.insert(insert);
                } else if (wxUnionidRelation.getStatus() == 0) {
                    wxUnionidRelationMapper.update(null, new LambdaUpdateWrapper<WxUnionidRelation>()
                        .eq(WxUnionidRelation::getId, wxUnionidRelation.getId()).set(WxUnionidRelation::getStatus, 1));
                }
            }
        } else if ("unsubscribe".equals(event)) {
            wxUnionidRelationMapper.update(null,
                new LambdaUpdateWrapper<WxUnionidRelation>().eq(WxUnionidRelation::getOpenid, fromUserName)
                    .set(WxUnionidRelation::getStatus, 0));
        }

        return uriParam.getEchostr();
    }

}
