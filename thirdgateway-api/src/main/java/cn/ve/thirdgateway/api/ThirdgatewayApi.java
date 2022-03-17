package cn.ve.thirdgateway.api;

import cn.ve.feign.pojo.CommonResult;
import cn.ve.thirdgateway.pojo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "commons-provider")
public interface ThirdgatewayApi {

    @PostMapping("/thirdgateway/admin/v1.0/idCardOcr")
    CommonResult<IdCardOcrResp> idCardOcr(@RequestBody AliOCRParam aliOCRParam);

    @PostMapping("/thirdgateway/admin/v1.0/bankCardOcr")
    CommonResult<BankCardOcrResp> bankCardOcr(@RequestBody AliOCRParam aliOCRParam);

    @PostMapping("/thirdgateway/admin/v1.0/bankCard3Factor")
    CommonResult<BankCard3FactorResp> bankCard3Factor(@RequestBody Ali3FactorParam param);

    @GetMapping("/thirdgateway/admin/v1.0/getOpenidByJscode")
    CommonResult<WechatOpenidDTO> getOpenidByJscode(@RequestParam("jscode") String jscode);

    @GetMapping("/thirdgateway/admin/v1.0/getPhoneByEncryptedData")
    CommonResult<String> getPhoneByEncryptedData(@RequestParam("data") String data, @RequestParam("secretKey") String secretKey,
        @RequestParam("ivString") String ivString);

    @PostMapping("/thirdgateway/admin/v1.0/officialAccountMsg")
    CommonResult<Object> officialAccountMsg(@RequestBody OfficialAccountMsgParam param);

    @PostMapping("/thirdgateway/admin/v1.0/sendSms")
    CommonResult<Object> sendSms();
}
