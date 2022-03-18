package cn.ve.user.api;

import cn.ve.feign.config.FeignConfiguration;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.user.dto.LoginSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user", configuration = FeignConfiguration.class)
public interface UserApi {

    @GetMapping("/feign/test")
    CommonResult<Boolean> test();

    @GetMapping("/feign/loginStatus")
    CommonResult<LoginSession> loginStatus();

}
