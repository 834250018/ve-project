package cn.ve.thirdparty.api;

import cn.ve.feign.pojo.CommonResult;
import cn.ve.thirdparty.pojo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "commons-provider")
public interface ThirdpartyApi {

    @PostMapping("/thirdparty/admin/v1.0/idCardOcr")
    CommonResult<IdCardOcrResp> idCardOcr(@RequestBody AliOCRParam aliOCRParam);

    @PostMapping("/thirdparty/admin/v1.0/bankCardOcr")
    CommonResult<BankCardOcrResp> bankCardOcr(@RequestBody AliOCRParam aliOCRParam);

    @PostMapping("/thirdparty/admin/v1.0/bankCard3Factor")
    CommonResult<BankCard3FactorResp> bankCard3Factor(@RequestBody Ali3FactorParam param);

    @GetMapping("/thirdparty/admin/v1.0/getOpenidByJscode")
    CommonResult<WechatOpenidDTO> getOpenidByJscode(@RequestParam("jscode") String jscode);

    @GetMapping("/thirdparty/admin/v1.0/getPhoneByEncryptedData")
    String getPhoneByEncryptedData(@RequestParam("data") String data, @RequestParam("secretKey") String secretKey,
        @RequestParam("ivString") String ivString);

    @PostMapping("/thirdparty/admin/v1.1.3/officialAccountMsg")
    void officialAccountMsg(@RequestBody OfficialAccountMsgParam param);

}
