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
 * 资源拦截Servlet
 * 主要用于在jar中提供页面给用户访问
 *
 * @author y3tu
 * @date 2018/10/25
 */
@Slf4j
public abstract class AbstractResourceServlet extends HttpServlet {

    /**
     * 资源路径
     */
    protected final String resourcePath;

    public AbstractResourceServlet(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public void init() throws ServletException {
        //一些初始化的工作
    }

    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }

    /**
     * 获取或者上传数据的请求处理，由子类实现
     *
     * @param request 请求
     * @param url
     * @return
     */
    protected abstract String process(HttpServletRequest request, String url);
    /**
     * 返回页面资源
     *
     * @param fileName
     * @param uri
     * @param response
     * @throws IOException
     */
    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response) throws IOException {
        String filePath = getFilePath(fileName);
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        if (StrUtil.containsAnyIgnoreCase(fileName, ".jpg", ".png", ".woff", ".ttf",".gif")) {
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

}
