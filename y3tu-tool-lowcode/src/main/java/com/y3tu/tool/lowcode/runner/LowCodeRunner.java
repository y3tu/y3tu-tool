package com.y3tu.tool.lowcode.runner;

import com.y3tu.tool.lowcode.common.util.Banner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 启动完成提示
 *
 * @author y3tu
 */
@Slf4j
@Component
public class LowCodeRunner implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (context.isActive()) {
            Banner.print();
            log.info("  _   _   _   _   _   _   _   _");
            log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
            log.info("( c | o | m | p | l | e | t | e )");
            log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
            log.info("Y3tu-Tool-Low-Code准备完毕，时间：{}", LocalDateTime.now());
        }
    }
}
