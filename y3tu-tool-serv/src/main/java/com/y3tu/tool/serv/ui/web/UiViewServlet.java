package com.y3tu.tool.serv.ui.web;

import cn.hutool.core.lang.UUID;
import com.y3tu.tool.core.http.ip.IPRange;
import com.y3tu.tool.core.http.servlet.AbstractResourceServlet;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.BeanCacheUtil;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.serv.ui.service.AuthService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author y3tu
 */
@Slf4j
public class UiViewServlet extends AbstractResourceServlet {

    private InitServletData initServletData = new InitServletData();


    public UiViewServlet() {
        super("y3tu-tool-ui");
    }


    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        // 权限校验
        String ip = request.getRemoteAddr();
        boolean security = BeanCacheUtil.getBean(AuthService.class).checkSecurity(initServletData, ip, path);
        if (!security) {
            response.sendRedirect(uri + "/nopermit.html");
            return;
        }
        //默认进入首页
        if ("/".equals(path) || "".equals(path)) {
            response.sendRedirect(uri + "/index.html");
            return;
        }
        //请求路径包含.json表示这是一个获取数据的请求
        if (path.contains(".json")) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(process(request, response, path));
            return;
        }

        //查找到资源文件并返回给前台
        try {
            returnResourceFile(path, uri, response);
        } catch (Exception e) {
            //查找资源返回错误暂不处理，记录异常
            log.error("查找资源异常", e);
            super.service(request, response);
        }
    }

    /**
     * 对请求的处理
     *
     * @param url
     * @return
     */
    @Override
    protected String process(HttpServletRequest request, HttpServletResponse response, String url) {
        try {

            //登录
            if (StrUtil.startWith(url, URLConstant.URL_LOGIN)) {
                if (checkLoginParam(request)) {
                    //登录成功
                    String token = UUID.randomUUID().toString();
                    return JsonUtil.toJson(R.success(token));
                } else {
                    return JsonUtil.toJson(R.error("用户名密码错误!"));
                }
            }

        } catch (Exception e) {
            log.error("请求异常", e);
            return JsonUtil.toJson(R.error("请求异常:" + e.getMessage()));
        }
        return JsonUtil.toJson(R.warn("服务端没有找到匹配的url:" + url));
    }

    @Override
    public void init() throws ServletException {
        super.init();
        initAuthEnv();
    }

    /**
     * 校验登录使用权限
     */
    private void initAuthEnv() {
        String paramUserName = getInitParameter(InitServletData.PARAM_NAME_USERNAME);
        if (StrUtil.isNotEmpty(paramUserName)) {
            this.initServletData.setUsername(paramUserName);
        }
        String paramPassword = getInitParameter(InitServletData.PARAM_NAME_PASSWORD);
        if (StrUtil.isNotEmpty(paramPassword)) {
            this.initServletData.setPassword(paramPassword);
        }
        String paramAllow = getInitParameter(InitServletData.PARAM_NAME_ALLOW);
        if (StrUtil.isNotEmpty(paramAllow)) {
            this.initServletData.setAllowList(parseStringToIP(paramAllow));
        }
        String paramDeny = getInitParameter(InitServletData.PARAM_NAME_DENY);
        if (StrUtil.isNotEmpty(paramDeny)) {
            this.initServletData.setDenyList(parseStringToIP(paramDeny));
        }
    }

    private List<IPRange> parseStringToIP(String ipStr) {
        List<IPRange> ipList = new ArrayList<>();
        if (ipStr != null && ipStr.trim().length() != 0) {
            ipStr = ipStr.trim();
            String[] items = ipStr.split(",");

            for (String item : items) {
                if (item == null || item.length() == 0) {
                    continue;
                }
                IPRange ipRange = new IPRange(item);
                ipList.add(ipRange);
            }
        }
        return ipList;
    }

    /**
     * 校验登录用户名密码
     *
     * @param request
     * @return
     */
    public boolean checkLoginParam(HttpServletRequest request) {
        String usernameParam = request.getParameter(InitServletData.PARAM_NAME_USERNAME);
        String passwordParam = request.getParameter(InitServletData.PARAM_NAME_PASSWORD);
        if (null == this.initServletData.getUsername() && null == this.initServletData.getPassword()) {
            return true;
        } else if (this.initServletData.getUsername().equals(usernameParam) && this.initServletData.getPassword().equals(passwordParam)) {
            return true;
        }
        return false;
    }

}
