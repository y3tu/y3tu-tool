package com.y3tu.tool.report.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/test")
    public String test() {
        return "succes1s";
    }
}
