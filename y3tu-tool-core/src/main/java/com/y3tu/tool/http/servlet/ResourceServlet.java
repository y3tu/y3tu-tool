package com.y3tu.tool.http.servlet;

import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.core.io.resource.ResourceUtil;
import com.y3tu.tool.core.util.CharsetUtil;
import com.y3tu.tool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author y3tu
 * @date 2018/10/25
 */
@Slf4j
public abstract class ResourceServlet extends HttpServlet {

    /**
     * 资源路径
     */
    protected final String resourcePath;

    /**
     * 拦截前缀地址
     */
    protected final String prefixPath;

    public ResourceServlet(String resourcePath, String prefixPath) {
        this.resourcePath = resourcePath;
        this.prefixPath = prefixPath;
    }

    @Override
    public void init() throws ServletException {
        //一些初始化的工作
    }

    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException,
            IOException {

        String filePath = getFilePath(fileName);

        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }

        if (StrUtil.containsAnyIgnoreCase(fileName, ".jpg", ".png", ".woff", ".ttf")) {
            byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream(filePath));
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }

            return;
        }

        String text = IoUtil.read(ResourceUtil.getStream(filePath), CharsetUtil.UTF_8);
        if (text == null) {
            response.sendRedirect(uri + "/index.html");
            return;
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        } else if (fileName.endsWith(".svg")) {
            response.setContentType("text/xml;charset=utf-8");
        }
        response.getWriter().write(text);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/" + prefixPath + "/index.html");
            } else {
                response.sendRedirect(prefixPath + "/index.html");
            }
            return;
        }

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        if (path.contains(".json")) {
            String fullUrl = path;
            if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }
            response.getWriter().print(process(fullUrl));
            return;
        }

        // find file in resources path
        returnResourceFile(path, uri, response);
    }

    protected abstract String process(String url);

}
