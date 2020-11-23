package com.y3tu.tool.test;

import com.y3tu.tool.report.annotation.EnableToolReport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableToolReport
public class ToolTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolTestApplication.class, args);
    }

}
