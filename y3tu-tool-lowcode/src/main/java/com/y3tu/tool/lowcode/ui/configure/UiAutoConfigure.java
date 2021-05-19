package com.y3tu.tool.lowcode.ui.configure;

import com.y3tu.tool.core.exception.ServerException;
import com.y3tu.tool.lowcode.ui.web.UiViewServlet;
import com.y3tu.tool.lowcode.ui.web.UiFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UI自动配置
 *
 * @author y3tu
 */
@Configuration
@EnableConfigurationProperties(UiProperties.class)
@ConditionalOnWebApplication
public class UiAutoConfigure {

    @Value("${y3tu.tool.cache.url-pattern:/y3tu-tool-cache/*}")
    private String cacheUrlPattern;

    private final String serverUrlPattern = "/y3tu-tool-lowcode/*";

    @Value("${y3tu.tool.server.ui.url-pattern:/tool-lowcode-ui/*}")
    private String uiUrlPattern;


    /**
     * 配置url拦截
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean uiViewServletRegistrationBean(UiProperties properties) {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        //设置需要拦截的url
        registrationBean.setServlet(new UiViewServlet());
        registrationBean.addUrlMappings(properties.getUrlPattern() != null ? properties.getUrlPattern() : "/tool-server-ui/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean cacheFilterRegistration() {

        if (uiUrlPattern.equals(serverUrlPattern)) {
            //如果用户配置的url和服务url相同，会影响url解析，应限制用户配置的url不能合serverUrlPattern相同
            throw new ServerException("用户配置的url不能为y3tu-tool-lowcode,请更换！");
        }

        FilterRegistrationBean registration = new FilterRegistrationBean();
        UiFilter uiFilter = new UiFilter();
        uiFilter.setCacheUrlPattern(cacheUrlPattern);
        uiFilter.setUiUrlPattern(uiUrlPattern);
        uiFilter.setServerUrlPattern(serverUrlPattern);
        registration.setFilter(uiFilter);
        registration.addUrlPatterns(uiUrlPattern);
        registration.setName("uiFilter");
        registration.setOrder(1);
        return registration;
    }


}
