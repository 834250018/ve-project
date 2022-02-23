package cn.ve.rest.pojo;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ve
 * @date 2021/8/2
 */
@Slf4j
public abstract class BaseController {

    protected Long getUserId() {
        // todo
        return 0L;
    }

    protected String getUserPhone() {
        // todo
        return "0L";
    }

    protected String getUserName() {
        // todo
        return "0L";
    }



    protected void clearCookies(HttpServletResponse response) {
//        addCookie(response, SessionCookie.Fields.expireAt, 1, 0);
//        addCookie(response, SessionCookie.Fields.accessToken, 1, 0);
//        addCookie(response, SessionCookie.Fields.loginId, 1, 0);
//        addCookie(response, SessionCookie.Fields.userId, 1, 0);
    }

    protected void addCookie(HttpServletResponse response, String key, Object value, int expiry) {
        Cookie cookie = new Cookie(key, String.valueOf(value));
        cookie.setHttpOnly(Boolean.TRUE);
        cookie.setPath("/");
        //        cookie.setDomain("localhost");
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }

}