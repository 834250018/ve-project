package cn.ve.user.controller;

import cn.ve.base.pojo.RequestHeaderHolder;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.user.api.UserApi;
import cn.ve.user.dto.LoginSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FeignController implements UserApi {

    @Override
    public CommonResult<Boolean> test() {
        log.info("远程调用成功");
        return CommonResult.success(Boolean.TRUE);
    }

    @Override
    public CommonResult<LoginSession> loginStatus() {
        // todo 从请求头中拿到token,然后用token获取redis中的敏感信息
        LoginSession data = new LoginSession();
        log.info("token:{}", RequestHeaderHolder.get().getToken());
        log.info("requestId:{}", RequestHeaderHolder.get().getRequestId());
        log.info("MDC:{}", MDC.get("requestId"));
        data.setUserId(0L);
        data.setLoginId(0L);
        data.setPhone("17322007051");
        data.setHeadPortrait("");
        data.setNickname("ve");
        return CommonResult.success(data);
    }

}

