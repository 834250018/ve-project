package cn.ve.user.controller;

import cn.ve.base.pojo.CommonResult;
import cn.ve.user.api.UserApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/feign")
@Api(value = "后台-登陆表", description = "后台-登陆表")
public class FeignController implements UserApi {

    @GetMapping("/test")
    public CommonResult<Boolean> test() {
        log.info("远程调用成功");
        return CommonResult.success(Boolean.TRUE);
    }

}

