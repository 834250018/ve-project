package cn.ve.thirdparty.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.ve.base.pojo.VeException;
import cn.ve.thirdparty.dal.entity.WxUnionidRelation;
import cn.ve.thirdparty.dal.mapper.WxUnionidRelationMapper;
import cn.ve.thirdparty.pojo.*;
import cn.ve.thirdparty.service.WechatService;
import cn.ve.thirdparty.util.WechatUtil;
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
    @Value("${wechat.appid.official-account}")
    private String officialAccountAppid;
    @Value("${wechat.secret.official-account}")
    private String officialAccountSecret;
    @Value("${wechat.message.template-id}")
    private String templateId;
    @Value("${wechat.access_token.url}")
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
        // 设置模板参数
        Map<String, DataBody> data = new HashMap<>();
        // 首行标题: first
        // 姓名: keyword1
        // 时间: keyword2
        // 状态: keyword3
        // 尾行备注: remark
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
            log.error("推送公众号消息失败:{}", e.getMessage(), e);
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
        // todo 这里改了请求client,需要测一下
        ResponseEntity<Map> objectResponseEntity = restTemplate.postForEntity(url, null, Map.class);
        if (objectResponseEntity.getStatusCode() != org.springframework.http.HttpStatus.OK) {
            log.error("请求微信服务异常_{}", objectResponseEntity.getStatusCode());
            throw new VeException("请求微信服务异常");
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
                    throw new VeException(400, "请求微信服务异常");
                }
                // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
                Integer subscribe = (Integer)queryUserEntity.getBody().get("subscribe");
                if (subscribe == 0) {
                    continue;
                }

                // 查询unionid并插入
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
        // 1.将token、timestamp、nonce三个参数进行字典序排序
        String collect =
            Stream.of(wechatOfficialAccountCallbackToken, uriParam.getTimestamp(), uriParam.getNonce()).sorted()
                .collect(Collectors.joining());
        // 2.将三个参数字符串拼接成一个字符串进行sha1加密
        String sha1 = DigestUtil.sha1Hex(collect);
        // 3.开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (!sha1.equals(uriParam.getSignature())) {
            log.error("sha1校验不通过");
            return null;
        }
        if (uriParam.getEchostr() != null) {
            return uriParam.getEchostr();
        }
        // 解析xml
        Map<String, String> map = WxPayKit.xmlToMap(xmlMsg);
        String encrypt = map.get("Encrypt");

        byte[] msgWithRandomBytes;
        try {
            byte[] secretKeyBytes = cn.hutool.core.codec.Base64.decode(wechatOfficialAccountAESKey + "=");
            msgWithRandomBytes =
                WechatUtil.decrypt(Base64.decode(encrypt), secretKeyBytes, Arrays.copyOfRange(secretKeyBytes, 0, 16));
        } catch (Exception e) {
            log.error("解密失败: {}", e.getMessage(), e);
            return null;
        }
        String msgWithRandomString = new String(msgWithRandomBytes, StandardCharsets.UTF_8);
        if (!msgWithRandomString.endsWith(officialAccountAppid)) {
            log.error("officialAccountAppid校验不通过");
            return null;
        }
        // 去掉后缀
        msgWithRandomString = msgWithRandomString.replaceAll(officialAccountAppid, "");
        byte[] msgWithPrefixBytes = msgWithRandomString.getBytes(StandardCharsets.UTF_8);
        // 去掉前缀
        byte[] msgBytes = new byte[msgWithPrefixBytes.length - 20];
        System.arraycopy(msgWithPrefixBytes, 20, msgBytes, 0, msgBytes.length);
        String xml = new String(msgBytes);
        Map<String, String> decryptData = WxPayKit.xmlToMap(xml);
        // 发送方帐号（一个OpenID）
        String fromUserName = decryptData.get("FromUserName");
        // 	事件类型，subscribe(订阅)、unsubscribe(取消订阅)
        String event = decryptData.get("Event");
        if ("subscribe".equals(event)) {
            // 查询unionid
            ResponseEntity<Map> forEntity = new RestTemplate().getForEntity(
                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAccessToken(officialAccountAppid,
                    officialAccountSecret) + "&openid=" + fromUserName, Map.class);
            if (forEntity.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                throw new VeException(400, "请求微信服务异常");
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
