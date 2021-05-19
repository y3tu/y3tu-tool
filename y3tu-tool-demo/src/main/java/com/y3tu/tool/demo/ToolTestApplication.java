package com.y3tu.tool.demo;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.y3tu.tool.lowcode.ui.annotation.EnableToolUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableToolUI
public class ToolTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolTestApplication.class, args);
    }

}
