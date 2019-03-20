package com.y3tu.tool.cache.config;


import com.y3tu.tool.cache.properties.LayerCacheProperties;
import com.y3tu.tool.cache.web.servlet.LayerCacheViewServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author yuhao.wang3
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "spring.y3tu.layer-cache.servlet-enable", havingValue = "true", matchIfMissing = false)
public class LayerCacheServletConfig {
    @Bean
    public ServletRegistrationBean statViewServletRegistrationBean(LayerCacheProperties properties) {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new LayerCacheViewServlet());
        registrationBean.addUrlMappings(!StringUtils.isEmpty(properties.getUrlPattern()) ? properties.getUrlPattern() : "/layer-cache/*");
        return registrationBean;
    }
}
