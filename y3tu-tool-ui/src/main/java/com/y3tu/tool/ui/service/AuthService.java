package com.y3tu.tool.ui.service;

import cn.hutool.core.collection.CollUtil;
import com.y3tu.tool.core.http.ip.IPAddress;
import com.y3tu.tool.core.http.ip.IPRange;
import com.y3tu.tool.ui.web.InitServletData;
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


}
