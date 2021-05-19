package com.y3tu.tool.lowcode.ui.web;

import com.y3tu.tool.core.util.StrUtil;
import lombok.Data;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UI 过滤器
 *
 * @author y3tu
 */
@Data
public class UiFilter implements Filter {

    private String cacheUrlPattern;
    private String lowCodeUrlPattern;
    private String uiUrlPattern;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUrl = httpRequest.getRequestURI();

        if (requestUrl.indexOf(formatUrl(cacheUrlPattern)) > 0) {
            requestUrl = StrUtil.replace(requestUrl, formatSuffixUrl(uiUrlPattern), "");
            httpRequest.getRequestDispatcher(requestUrl).forward(httpRequest, httpResponse);
        } else if (requestUrl.indexOf(formatUrl(lowCodeUrlPattern)) > 0) {
            int urlIndex =  requestUrl.indexOf(formatSuffixUrl(uiUrlPattern));
            requestUrl = requestUrl.substring(urlIndex,requestUrl.length());
            requestUrl = StrUtil.replace(requestUrl, formatSuffixUrl(uiUrlPattern), "");
            httpRequest.getRequestDispatcher(requestUrl).forward(httpRequest, httpResponse);
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {

    }

    private String formatSuffixUrl(String path) {
        if (StrUtil.endWith(path, "*")) {
            path = StrUtil.subPre(path, path.length() - 2);
        }
        return path;
    }

    private String formatPrefixUrl(String path) {
        if (StrUtil.startWith(path, "/")) {
            path = StrUtil.subSuf(path, 1);
        }
        return path;
    }

    private String formatUrl(String path) {
        path = formatSuffixUrl(path);
        path = formatPrefixUrl(path);
        return path;
    }

}
