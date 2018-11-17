package com.y3tu.tool.admin.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * admin自动配置
 *
 * @author y3tu
 * @date 2018/11/9
 */
@Configuration
@Import(ToolAdminServletConfig.class)
public class ToolAdminAutoConfig {

    @Bean
    public ServletRegistrationBean statViewServletRegistrationBean() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new ToolAdminViewServlet());
        registrationBean.addUrlMappings("/y3tu-tool/*");
        return registrationBean;
    }
}
