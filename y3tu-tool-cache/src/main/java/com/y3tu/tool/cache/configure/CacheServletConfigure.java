package com.y3tu.tool.cache.configure;

import com.y3tu.tool.cache.core.servlet.LayeringCacheServlet;
import com.y3tu.tool.cache.properties.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.servlet.Servlet;

/**
 * @author yuhao.wang3
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "yao.tool.cache.servlet-enabled", havingValue = "true", matchIfMissing = false)
public class CacheServletConfigure {
    @Bean
    public ServletRegistrationBean<Servlet> layeringCacheStatViewServletRegistrationBean(CacheProperties properties) {
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new LayeringCacheServlet());
        registrationBean.addUrlMappings(!StringUtils.isEmpty(properties.getUrlPattern()) ? properties.getUrlPattern() : "/tool-cache/*");
        return registrationBean;
    }
}
