package com.y3tu.tool.report.config;

import com.y3tu.tool.report.http.ReportViewServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 报表页面映射配置
 *
 * @author y3tu
 * @date 2018/10/25
 */
@Configuration
@ConditionalOnWebApplication
public class ReportViewServletConfiguration {

    @Bean
    public ServletRegistrationBean reportToolViewServletRegistrationBean() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new ReportViewServlet());
        registrationBean.addUrlMappings("/report-tool/*");
        return registrationBean;
    }
}
