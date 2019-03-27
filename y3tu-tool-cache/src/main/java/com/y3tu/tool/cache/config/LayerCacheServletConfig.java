package com.y3tu.tool.cache.config;


import com.y3tu.tool.cache.properties.LayerCacheProperties;
import com.y3tu.tool.cache.web.servlet.LayerCacheViewServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * 根据配置开启多级缓存Servlet界面功能
 *
 * @author yuhao.wang3
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "y3tu-tool-cache.servlet-enable", havingValue = "true", matchIfMissing = false)
public class LayerCacheServletConfig {
    @Bean
    public ServletRegistrationBean statViewServletRegistrationBean(LayerCacheProperties properties) {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        //设置拦截url后的处理servlet
        registrationBean.setServlet(new LayerCacheViewServlet());
        //设置需要拦截的url
        registrationBean.addUrlMappings(!StringUtils.isEmpty(properties.getUrlPattern()) ? properties.getUrlPattern() : "/layer-cache/*");
        return registrationBean;
    }
}
