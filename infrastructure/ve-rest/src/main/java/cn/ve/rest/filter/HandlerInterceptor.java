package cn.ve.rest.filter;

import cn.hutool.core.util.NumberUtil;
import cn.ve.base.pojo.*;
import cn.ve.base.util.IdUtil;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.user.api.UserApi;
import cn.ve.user.dto.LoginSession;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.slf4j.MDC;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserApi userApi;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        // 设置requestId
        initRequestId(request);

        // 如果是feign调用,holder->header
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Class<?>[] c = handlerMethod.getBeanType().getInterfaces();
        for (Class<?> aClass : c) {
            boolean feignStatus = aClass.isAnnotationPresent(FeignClient.class);
            if (feignStatus) {
                setFeignRequestHeader(request);
                return true;
            }
        }

        // 检查权限cookie->userInfo->holder
        boolean ignoreAuthCheck = handlerMethod.getMethod().isAnnotationPresent(AuthIgnore.class);
        if (ignoreAuthCheck) {
            return true;
        }

        checkAuth(request);
        return true;
    }

    private void initRequestId(HttpServletRequest request) {
        String requestId = getFromRequest(request, RequestHeader.Fields.requestId);
        if (StringUtils.isBlank(requestId)) {
            requestId = IdUtil.genUUID();
        }
        MDC.put(RequestHeader.Fields.requestId, requestId);
        RequestHeaderHolder.get().setRequestId(requestId);
    }

    private void setFeignRequestHeader(HttpServletRequest request) {
        RequestHeader requestHeader = RequestHeaderHolder.get();
        requestHeader.setRequestId(getFromRequest(request, RequestHeader.Fields.requestId));
        requestHeader.setToken(getFromRequest(request, RequestHeader.Fields.token));
        requestHeader.setLoginId(getLongFromRequest(request, RequestHeader.Fields.loginId));
        requestHeader.setUserId(getLongFromRequest(request, RequestHeader.Fields.userId));
        requestHeader.setNickname(getFromRequest(request, RequestHeader.Fields.nickname));
        requestHeader.setHeadPortrait(getFromRequest(request, RequestHeader.Fields.headPortrait));
        requestHeader.setRequestTime(getLongFromRequest(request, RequestHeader.Fields.requestTime));
    }

    private void checkAuth(HttpServletRequest request) {
        String token = getFromRequest(request, RequestHeader.Fields.token);
        if (StringUtils.isBlank(token)) {
            throw new VeBaseException(VeResultCode.USER_NOT_LOGIN);
        }
        RequestHeader requestHeader = RequestHeaderHolder.get();
        requestHeader.setToken(token);

        CommonResult<LoginSession> loginSessionCommonResult = userApi.loginStatus();
        if (loginSessionCommonResult.getCode() != HttpStatus.OK.value()) {
            throw new VeBaseException(VeResultCode.USER_NOT_LOGIN);
        }

        LoginSession loginSession = loginSessionCommonResult.getData();
        requestHeader.setHeadPortrait(getFromRequest(request, RequestHeader.Fields.headPortrait));
        requestHeader.setUserId(loginSession.getUserId());
        requestHeader.setLoginId(loginSession.getLoginId());
        requestHeader.setHeadPortrait(loginSession.getHeadPortrait());
        requestHeader.setNickname(loginSession.getNickname());

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        RequestHeaderHolder.clear();
    }

    /**
     * 按顺序从三个来源获取参数:参数,请求头,cookie
     *
     * @param request
     * @param key
     * @return 字符串
     */
    private String getFromRequest(HttpServletRequest request, String key) {
        // 从参数中拿
        String valueString = request.getParameter(key);
        if (StringUtils.isNotBlank(valueString)) {
            return valueString;
        }

        // 从请求头中拿
        valueString = request.getHeader(key);
        if (StringUtils.isNotBlank(valueString)) {
            return valueString;
        }

        // 从cookie里面拿
        Cookie[] cookies = request.getCookies();
        if (Arrays.isNullOrEmpty(cookies)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;

    }

    /**
     * 按顺序从三个来源获取参数:参数,请求头,cookie
     *
     * @param request
     * @param key
     * @return 长整型
     */
    private Long getLongFromRequest(HttpServletRequest request, String key) {
        String valueString = getFromRequest(request, key);
        if (StringUtils.isBlank(valueString)) {
            return null;
        }
        if (NumberUtil.isLong(valueString)) {
            return Long.valueOf(valueString);
        } else {
            return null;
        }
    }
}