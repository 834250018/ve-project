package cn.ve.thirdgateway.controller;

import cn.ve.thirdgateway.pojo.UriParam;
import cn.ve.thirdgateway.service.WechatService;
import com.ijpay.core.kit.HttpKit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ve
 * @date 2021/8/6
 */
@Slf4j
@RequestMapping("/face")
@RestController
@Validated
public class WechatController {

    @Resource
    private WechatService wxService;

    //    @ReturnIgnore todo 需要处理响应,解包
    @ApiOperation(value = "微信回调")
    @RequestMapping(value = "wechatCallback", method = {RequestMethod.GET, RequestMethod.POST})
    public Long wechatCallback(UriParam uriParam, HttpServletRequest request) {
        // todo 接口应该拆成两个,一个是get请求,用于第一次绑定校验,一个是post请求,用于公众号实际回调
        String xmlMsg = HttpKit.readData(request);
        return wxService.wechatCallback(uriParam, xmlMsg);
    }

}