package cn.ve.user.api;

import cn.ve.base.pojo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user")
public interface UserApi {

    @GetMapping("/feign/test")
    CommonResult<Boolean> test();

}
