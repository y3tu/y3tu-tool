package com.y3tu.tool.ui.web.filter;

import com.y3tu.tool.core.util.StrUtil;
import lombok.AllArgsConstructor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author y3tu
 * @date 2019-04-09
 */
@AllArgsConstructor
public class CacheFilter implements Filter {

    private String cacheUrlPattern;
    private String uiUrlPattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        cacheUrlPattern = formatUrl(cacheUrlPattern);
        uiUrlPattern = formatUrl(uiUrlPattern);
        String requestUrl = httpRequest.getRequestURI();
        if (requestUrl.indexOf("cache-stats") > 0) {
            requestUrl = StrUtil.replace(requestUrl, uiUrlPattern, cacheUrlPattern);
            httpRequest.getRequestDispatcher(requestUrl).forward(httpRequest, httpResponse);
            return;
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {

    }


    private String formatUrl(String path) {
        if (StrUtil.endWith(path, "*")) {
            path = StrUtil.subPre(path, path.length() - 2);
        }
        if (StrUtil.startWith(path, "/")) {
            path = StrUtil.subSuf(path, 1);
        }
        return path;
    }

}
