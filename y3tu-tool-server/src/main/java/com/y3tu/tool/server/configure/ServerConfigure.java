package com.y3tu.tool.server.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 服务配置
 *
 * @author y3tu
 */
@Configuration
@ComponentScan("com.y3tu.tool.server")
public class ServerConfigure {

    /**
     * 开启WebSocket支持
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
