package com.y3tu.tool.test;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class ToolTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolTestApplication.class, args);
    }

}
