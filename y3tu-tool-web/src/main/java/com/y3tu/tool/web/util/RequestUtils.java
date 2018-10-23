package com.y3tu.tool.web.util;

import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.http.IpUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * HttpServletRequest帮助类
 *
 * @author liuht
 * @date 2017/12/22 9:08
 */
@SuppressWarnings("all")
public class RequestUtils {

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
}
