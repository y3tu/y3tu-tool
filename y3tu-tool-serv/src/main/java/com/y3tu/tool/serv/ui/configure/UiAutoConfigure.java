package com.y3tu.tool.serv.ui.configure;

import com.y3tu.tool.serv.ui.web.UiViewServlet;
import com.y3tu.tool.serv.ui.web.filter.UiFilter;
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

    @Value("${y3tu.tool.report.url-pattern:/y3tu-tool-report/*}")
    private String reportUrlPattern;

    @Value("${y3tu.tool.ui.url-pattern:/y3tu-tool-ui/*}")
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
        registrationBean.addUrlMappings(properties.getUrlPattern() != null ? properties.getUrlPattern() : "/y3tu-tool-ui/*");

        if (properties.getAllow() != null) {
            registrationBean.addInitParameter("allow", properties.getAllow());
        }
        if (properties.getDeny() != null) {
            registrationBean.addInitParameter("deny", properties.getDeny());
        }
        if (properties.getLoginUsername() != null) {
            registrationBean.addInitParameter("loginUsername", properties.getLoginUsername());
        }
        if (properties.getLoginPassword() != null) {
            registrationBean.addInitParameter("loginPassword", properties.getLoginPassword());
        }
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean cacheFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        UiFilter uiFilter = new UiFilter();
        uiFilter.setCacheUrlPattern(cacheUrlPattern);
        uiFilter.setUiUrlPattern(uiUrlPattern);
        uiFilter.setReportUrlPattern(reportUrlPattern);
        registration.setFilter(uiFilter);
        registration.addUrlPatterns(uiUrlPattern);
        registration.setName("uiFilter");
        registration.setOrder(1);
        return registration;
    }


}
