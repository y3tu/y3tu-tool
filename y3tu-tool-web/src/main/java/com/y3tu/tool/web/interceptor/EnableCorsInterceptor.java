package com.y3tu.tool.web.interceptor;

import com.y3tu.tool.web.annotation.EnableMethodCors;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 允许跨域访问拦截器
 *
 * @author y3tu
 * @date 2018/10/30
 */
public class EnableCorsInterceptor implements HandlerInterceptor {
    private String allowOrigin = "*";
    private String allowMethods = "GET,POST,OPTIONS";
    private String allowHeaders = "Content-Type, Content-Length, X-Requested-With";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            EnableMethodCors config = method.getMethod().getAnnotation(EnableMethodCors.class);
            if (config != null) {
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowMethods);
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
