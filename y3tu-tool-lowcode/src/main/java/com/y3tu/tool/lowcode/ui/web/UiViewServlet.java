package com.y3tu.tool.lowcode.ui.web;

import com.y3tu.tool.core.http.servlet.AbstractResourceServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author y3tu
 */
@Slf4j
public class UiViewServlet extends AbstractResourceServlet {

    public UiViewServlet() {
        super("lowcode-ui");
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

        //默认进入首页
        if ("/".equals(path) || "".equals(path)) {
            response.sendRedirect(uri + "/index.html");
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


    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected String process(HttpServletRequest request, HttpServletResponse response, String url) {
        return null;
    }

}
