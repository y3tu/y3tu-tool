package com.y3tu.tool.web.util;

import cn.hutool.core.codec.Base64;
import com.y3tu.tool.core.exception.BusinessException;
import com.y3tu.tool.core.exception.ErrorEnum;
import com.y3tu.tool.core.util.CharsetUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.rmi.ServerException;

/**
 * 认证授权解析请求header信息
 *
 * @author y3tu
 * @date 2018/10/4
 */
public class AuthUtils {

    private static final String BASIC_ = "Basic ";

    /**
     * 从header 请求中的clientId/clientsecect
     *
     * @param header header中的参数
     * @throws ServerException if the Basic header is not present or is not valid
     *                         Base64
     */
    public static String[] extractAndDecodeHeader(String header)
            throws IOException {
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Failed to decode basic authentication token", ErrorEnum.UTIL_EXCEPTION);
        }

        String token = new String(decoded, CharsetUtil.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BusinessException("Invalid basic authentication token" + ErrorEnum.UTIL_EXCEPTION);
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * *从header 请求中的clientId/clientsecect
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String[] extractAndDecodeHeader(HttpServletRequest request)
            throws IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BASIC_)) {
            throw new BusinessException("请求头中client信息为空" + ErrorEnum.UTIL_EXCEPTION);
        }

        return extractAndDecodeHeader(header);
    }
}
