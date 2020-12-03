package com.y3tu.tool.cache.runner;

import com.y3tu.tool.cache.service.ToolCacheService;
import com.y3tu.tool.cache.staticdata.StaticDataCollect;
import com.y3tu.tool.cache.staticdata.StaticDataConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
    ToolCacheService cacheService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (context.isActive()) {

            //todo 加载静态数据 对于一些经常用到又不经常变动的数据，可以在程序启动后加载到内存中
            //todo 1.从配置文件中获取需要加载的表数据和sql配置 2.从数据库中加载配置

            log.info("开始加载静态资料...");
            StaticDataCollect dataCollect = new StaticDataCollect(cacheService);
            dataCollect.dataCollect(dataCollect.readHandler());


            log.info("  _   _   _   _   _   _   _   _");
            log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
            log.info("( c | o | m | p | l | e | t | e )");
            log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
            log.info("Y3tu-Tool-Cache准备完毕，时间：{}", LocalDateTime.now());

        }
    }
}
