package com.y3tu.tool.ui.config;

import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.ui.web.UiViewServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UI自动配置
 *
 * @author y3tu
 * @date 2019-03-28
 */
@Configuration
@EnableConfigurationProperties(UiProperties.class)
@ConditionalOnWebApplication
public class UiAutoConfiguration {

    @Autowired
    UiProperties properties;

    @Bean
    public ServletRegistrationBean uiViewServletRegistrationBean() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();

        //设置需要拦截的url
        String url = "/y3tu-tool-ui/*";
        if (!StrUtil.isEmpty(properties.getUrlPattern())) {
            url = properties.getUrlPattern();
            if (!StrUtil.endWith(url, '*')) {
                throw new ToolException(StrUtil.format("拦截路径格式不正确:{},eg:/y3tu-tool-ui/*", url));
            }
            if (!StrUtil.startWith(url, '/')) {
                throw new ToolException(StrUtil.format("拦截路径格式不正确:{},eg:/y3tu-tool-ui/*", url));
            }
        }
        //截取url 把/y3tu-tool-ui/*变成y3tu-tool-ui
        String path = StrUtil.sub(url, 1, -2);
        //设置拦截url后的处理servlet
        registrationBean.setServlet(new UiViewServlet(path));
        registrationBean.addUrlMappings(url);
        return registrationBean;
    }

}
