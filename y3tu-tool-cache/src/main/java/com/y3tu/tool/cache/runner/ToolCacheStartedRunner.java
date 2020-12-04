package com.y3tu.tool.cache.runner;

import com.y3tu.tool.cache.staticdata.StaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * cache的一些初始化操作
 *
 * @author y3tu
 */
@Slf4j
@Component
public class ToolCacheStartedRunner implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    StaticDataService staticDataService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (context.isActive()) {

            //加载配置isStartUp=true的静态数据
            staticDataService.dataCollect(staticDataService.readHandler().stream().filter(staticDataConfig -> staticDataConfig.isStartUp()).collect(Collectors.toList()));

            log.info("  _   _   _   _   _   _   _   _");
            log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
            log.info("( c | o | m | p | l | e | t | e )");
            log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
            log.info("Y3tu-Tool-Cache准备完毕，时间：{}", LocalDateTime.now());

        }
    }
}
