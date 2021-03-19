package com.y3tu.tool.serv.ui.web;

import com.y3tu.tool.core.http.ip.IPRange;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Servlet 初始化数据
 */
@Data
public class InitServletData {
    public static final String PARAM_NAME_TOKEN = "token";
    public static final String PARAM_NAME_USERNAME = "loginUsername";
    public static final String PARAM_NAME_PASSWORD = "loginPassword";
    public static final String PARAM_NAME_ALLOW = "allow";
    public static final String PARAM_NAME_DENY = "deny";

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;
    /**
     * 白名单
     */
    private List<IPRange> allowList = new ArrayList<>();

    /**
     * 黑名单（优先级高于白名单）
     */
    private List<IPRange> denyList = new ArrayList<>();
}
