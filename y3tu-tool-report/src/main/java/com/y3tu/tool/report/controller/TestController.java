package com.y3tu.tool.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author y3tu
 * @date 2018/10/25
 */
@Controller
@Slf4j
public class TestController {


    @GetMapping("/report/test")
    public String test() {
        return "test";
    }
}
