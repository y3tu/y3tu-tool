package com.y3tu.tool.ui;

import com.y3tu.tool.cache.annotation.EnableToolLayerCache;
import com.y3tu.tool.cache.annotation.EnableToolRedis;
import com.y3tu.tool.web.annotation.EnableGlobalCors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author y3tu
 * @date 2019-04-01
 */
@SpringBootApplication
@EnableGlobalCors
@EnableToolRedis
@EnableToolLayerCache
public class UiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }
}

