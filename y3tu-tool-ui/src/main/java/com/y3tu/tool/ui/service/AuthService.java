package com.y3tu.tool.ui.service;

import com.y3tu.tool.core.collection.CollUtil;
import com.y3tu.tool.http.ip.IPAddress;
import com.y3tu.tool.http.ip.IPRange;
import com.y3tu.tool.ui.web.InitServletData;
import com.y3tu.tool.ui.web.URLConstant;
import org.springframework.util.CollectionUtils;

/**
 * 权限校验
 */
public class AuthService {
    /**
     * 权限校验
     *
     * @param initServletData {@link InitServletData}
     * @param ip              IP地址
     * @param path            path
     */
    public boolean checkSecurity(InitServletData initServletData, String ip, String path) {
        // 不需要权限的就可以访问的资源
        if (isIgnoreSource(path)) {
            return true;
        }

        boolean ipV6 = ip != null && ip.indexOf(':') != -1;
        if (ipV6) {
            return "0:0:0:0:0:0:0:1".equals(ip);
        }

        // 检查是否是授权IP
        if (CollUtil.isEmpty(initServletData.getDenyList()) && CollectionUtils.isEmpty(initServletData.getAllowList())) {
            return true;
        }

        IPAddress ipAddress = new IPAddress(ip);

        for (IPRange range : initServletData.getDenyList()) {
            if (range.isIPAddressInRange(ipAddress)) {
                return false;
            }
        }

        if (initServletData.getAllowList().size() > 0) {
            for (IPRange range : initServletData.getAllowList()) {
                if (range.isIPAddressInRange(ipAddress)) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    /**
     * 忽略权限检查的资源
     *
     * @param path 地址
     * @return boolean
     */
    public boolean isIgnoreSource(String path) {
        // 不需要权限的就可以访问的资源
        return URLConstant.USER_LOGIN_PAGE.equals(path)
                || URLConstant.USER_SUBMIT_LOGIN.equals(path)
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/fonts/")
                || path.startsWith("/i/");
    }

}
