package cn.ve.thirdparty.controller;

import cn.ve.base.pojo.VeException;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.thirdparty.api.ThirdpartyApi;
import cn.ve.thirdparty.pojo.*;
import cn.ve.thirdparty.service.WechatService;
import cn.ve.thirdparty.util.AliUtil;
import cn.ve.thirdparty.util.WechatUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * @author ve
 * @date 2021/8/2
 */
@Slf4j
@RestController
public class FeignController implements ThirdpartyApi {

    @Value("${ali.app-key}")
    private String aliAppKey;
    @Value("${ali.app-secret}")
    private String aliAppSecret;
    @Value("${wechat.appid.miniprogram}")
    private String wechatMiniprogramAppid;
    @Value("${wechat.secret.miniprogram}")
    private String wechatMiniprogramSecret;
    @Value("${wechat.url.jscode2session}")
    private String jscode2sessionUrl;
    @Resource
    private WechatService wechatService;

    @Override
    public CommonResult<IdCardOcrResp> idCardOcr(@RequestBody AliOCRParam aliOCRParam) {
        if (StringUtils.isBlank(aliOCRParam.getImageBase64())) {
            return CommonResult.fail(400, "ImageBase64不能为空");
        }
        if (StringUtils.isBlank(aliOCRParam.getSide())) {
            return CommonResult.fail(400, "Side不能为空");
        }
        try {
            return CommonResult.success(
                new AliUtil(aliAppKey, aliAppSecret).idCardOcr(aliOCRParam.getImageBase64(), aliOCRParam.getSide()));
        } catch (Exception e) {
            log.error("身份证ocr识别失败: {}", e.getMessage(), e);
            return CommonResult.fail(500, "身份证ocr识别失败");
        }
    }

    @Override
    public CommonResult<BankCardOcrResp> bankCardOcr(@RequestBody AliOCRParam aliOCRParam) {
        if (StringUtils.isBlank(aliOCRParam.getImageBase64())) {
            throw new VeException(400, "ImageBase64不能为空");
        }
        return CommonResult.success(new AliUtil(aliAppKey, aliAppSecret).bankCardOcr(aliOCRParam.getImageBase64()));
    }

    @Override
    public CommonResult<BankCard3FactorResp> bankCard3Factor(@RequestBody Ali3FactorParam param) {
        try {
            return CommonResult.success(new AliUtil(aliAppKey, aliAppSecret)
                .bankCard3FactorAuth(param.getBankcard(), param.getIdcard(), param.getName()));
        } catch (Exception e) {
            return CommonResult.fail(500, e.getMessage());
        }
    }

    @Override
    public CommonResult<WechatOpenidDTO> getOpenidByJscode(@RequestParam("jscode") String jscode) {
        String resp = new RestTemplate().getForObject(
            MessageFormat.format(jscode2sessionUrl, wechatMiniprogramAppid, wechatMiniprogramSecret, jscode),
            String.class);
        return CommonResult.success(JSON.parseObject(resp, WechatOpenidDTO.class));
    }

    @Override
    public String getPhoneByEncryptedData(@RequestParam("data") String data,
        @RequestParam("secretKey") String secretKey, @RequestParam("ivString") String ivString) {
        WechatUserDTO decrypt = WechatUtil.decrypt(data, secretKey, ivString);
        if (!wechatMiniprogramAppid.equals(decrypt.getWatermark().getAppid())) {
            log.error("没找到对应appid:{}", decrypt.getWatermark().getAppid());
            throw new VeException(500, "没找到对应appid");
        }
        log.info("水印时间:{}", decrypt.getWatermark().getTimestamp());
        return decrypt.getPurePhoneNumber();
    }

    @Override
    @PostMapping("/admin/v1.1.3/officialAccountMsg")
    public void officialAccountMsg(@RequestBody OfficialAccountMsgParam param) {
        wechatService.officialAccountMsg(param);
    }

}
