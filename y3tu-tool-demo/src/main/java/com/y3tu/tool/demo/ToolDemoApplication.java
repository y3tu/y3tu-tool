package com.y3tu.tool.demo;

import com.y3tu.tool.cache.annotation.EnableToolCache;
import com.y3tu.tool.ui.annotation.EnableToolUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableToolCache
@EnableToolUI
public class ToolDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolDemoApplication.class, args);
    }

}
