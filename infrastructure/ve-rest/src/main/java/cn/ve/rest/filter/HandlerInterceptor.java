package cn.ve.rest.filter;

import cn.hutool.core.util.NumberUtil;
import cn.ve.base.pojo.*;
import cn.ve.base.util.IdUtil;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.user.api.UserApi;
import cn.ve.user.dto.LoginSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserApi userApi;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        // 设置requestId
        String requestId = getParamOrHeader(request, RequestHeader.Fields.requestId);
        if (StringUtils.isBlank(requestId)) {
            requestId = IdUtil.genUUID();
        }
        MDC.put(RequestHeader.Fields.requestId, requestId);

        // 如果是feign调用,holder->header
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Class<?>[] c = handlerMethod.getBeanType().getInterfaces();
        for (Class<?> aClass : c) {
            boolean feignStatus = aClass.isAnnotationPresent(FeignClient.class);
            if (feignStatus) {
                setFeignRequestHeader(request, requestId);
                return true;
            }
        }

        // todo 如果不是feign调用,通过token拿到用户信息同时设置进headerHolder
        // 检查权限cookie->userInfo->holder
        boolean ignoreAuthCheck = handlerMethod.getMethod().isAnnotationPresent(AuthIgnore.class);
        if (!ignoreAuthCheck) {
            checkAuth(request);
        }

        return true;
    }

    private void setFeignRequestHeader(HttpServletRequest request, String requestId) {
        RequestHeader requestHeader = RequestHeaderHolder.get();
        requestHeader.setRequestId(getParamOrHeader(request, requestId));
        requestHeader.setLoginId(getParamOrHeaderLong(request, RequestHeader.Fields.loginId));
        requestHeader.setUserId(getParamOrHeaderLong(request, RequestHeader.Fields.userId));
        requestHeader.setNickname(getParamOrHeader(request, RequestHeader.Fields.nickname));
        requestHeader.setHeadPortrait(getParamOrHeader(request, RequestHeader.Fields.headPortrait));
    }

    private void checkAuth(HttpServletRequest request) {
        CommonResult<LoginSession> loginSessionCommonResult = userApi.loginStatus();
        if (loginSessionCommonResult.getCode() != HttpStatus.OK.value()) {
            throw new VeBaseException(VeResultCode.USER_NOT_LOGIN);
        }
        // todo
        String token = getParamOrHeader(request, RequestHeader.Fields.token);

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        super.afterCompletion(request, response, handler, ex);
        // todo 清空requestHeaderHolder
    }

    private String getParamOrHeader(HttpServletRequest request, String key) {
        String valueString = request.getParameter(key);
        if (StringUtils.isEmpty(valueString)) {
            valueString = request.getHeader(key);
        }
        return valueString;
    }

    private Long getParamOrHeaderLong(HttpServletRequest request, String key) {
        String valueString = request.getParameter(key);
        if (StringUtils.isEmpty(valueString)) {
            valueString = request.getHeader(key);
        }
        if (NumberUtil.isLong(valueString)) {
            return Long.valueOf(valueString);
        } else {
            return null;
        }
    }
}