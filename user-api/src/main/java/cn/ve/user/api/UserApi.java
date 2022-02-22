package cn.ve.user.api;

import cn.ve.feign.pojo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user")
public interface UserApi {

    @GetMapping("/feign/test")
    CommonResult<Boolean> test();

}
