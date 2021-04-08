package com.y3tu.tool.server.generator.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("${y3tu.tool.server.ui.urlPattern:y3tu-tool-server}/generator")
@Slf4j
public class GeneratorController {
}
