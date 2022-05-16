package cn.ve.thirdgateway.controller;

import cn.ve.base.pojo.RequestHeaderHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ve
 * @date 2021/8/6
 */
@Slf4j
@RequestMapping
@RestController
@Validated
public class PublicController {

    @GetMapping("/test")
    public void test() {
        log.info("token:{}", RequestHeaderHolder.get().getToken());
        log.info("requestId:{}", RequestHeaderHolder.get().getRequestId());
        log.info("MDC:{}", MDC.get("requestId"));
    }


    @PostMapping("/test1")
    public void test1() {
        log.info("token:{}", RequestHeaderHolder.get().getToken());
        log.info("requestId:{}", RequestHeaderHolder.get().getRequestId());
        log.info("MDC:{}", MDC.get("requestId"));
    }

}