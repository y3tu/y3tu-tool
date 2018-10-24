package com.y3tu.tool.web.util;

import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.core.text.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Http与Servlet工具类.
 *
 * @author ThinkGem
 * @date 2017/12/22 9:08
 */
@SuppressWarnings("all")
public class ServletUtils {

    public static String getQueryString(HttpServletRequest request, String... exclude) {
        StringBuffer sb = new StringBuffer();
        List<String> excludeList = Arrays.asList(exclude);
        Enumeration<String> names = request.getParameterNames();
        String name;
        String value;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            value = StringUtils.join(request.getParameterValues(name));
            if (!excludeList.contains(name)) {
                sb.append("&").append(name).append("=")
                        .append(value);
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * 获取访问路径
     *
     * @param request HttpServletRequest
     * @return 访问路径 base url
     */
    public static String getBaseURL(HttpServletRequest request) {
        String path = request.getContextPath();
        return request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }

    /**
     * 将request请求中的参数及值转成一个Map格式.
     *
     * @param request HttpServletRequest
     * @return request中的请求及参数 request map
     */
    public static Map getRequestMap(HttpServletRequest request) {
        Map<String, Object> dto = MapUtil.newHashMap(16);
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = Objects.toString(enumeration.nextElement());
            String[] values = request.getParameterValues(name);

            Object val;
            if (values.length == 1) {
                val = values[0];
            } else {
                val = Arrays.asList(values);
            }
            dto.put(name, val);
        }
        return dto;
    }

    /**
     * 判断是否为 AJAX 请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.containsAnyIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (StringUtils.containsAnyIgnoreCase(ajax, "json", "xml")) {
            return true;
        }

        return false;
    }

    /**
     * 支持AJAX的页面跳转
     */
    public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            if (ServletUtils.isAjaxRequest(request)) {
                request.getRequestDispatcher(url).forward(request, response); // AJAX不支持Redirect改用Forward
            } else {
                response.sendRedirect(request.getContextPath() + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置客户端缓存过期时间 的Header.
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header, set a fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header, set a time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }
}
