package cn.summerwaves.interceptor;

import cn.summerwaves.util.CookieUtil;
import cn.summerwaves.util.TokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cookie token = CookieUtil.getCookieByName(httpServletRequest, "token");
        if (token == null || !TokenUtil.checkToken(token.getValue())) {
            httpServletRequest.getRequestDispatcher("/redirect").forward(httpServletRequest, httpServletResponse);
            if (token != null) {
                token.setMaxAge(0);
            }
            return false;
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
