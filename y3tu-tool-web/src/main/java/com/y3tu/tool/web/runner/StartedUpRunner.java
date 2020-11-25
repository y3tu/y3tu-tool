package com.y3tu.tool.web.runner;

import cn.hutool.core.util.ReflectUtil;
import com.y3tu.tool.web.sql.JdbcTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 启动完成后提示和一些业务初始化操作
 *
 * @author y3tu
 */
@Slf4j
@Component
public class StartedUpRunner implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context;


    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            log.info("  _   _   _   _   _   _   _   _");
            log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
            log.info("( c | o | m | p | l | e | t | e )");
            log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
            log.info("启动完毕，时间：{}", LocalDateTime.now());
        }


        //获取spring容器中所有的数据源bean
        String[] beanNames = context.getBeanNamesForType(DataSource.class);
        for (String name : beanNames) {
            DataSource dataSource = context.getBean(name, DataSource.class);
            //dataSourceMap是dynamic-datasource多数据源
            Map dataSourceMap = (Map) ReflectUtil.getFieldValue(dataSource, "dataSourceMap");
            if (dataSourceMap != null && dataSourceMap.keySet().size() > 0) {
                for (Object key : dataSourceMap.keySet()) {
                    JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) dataSourceMap.get(key));
                    JdbcTemplateUtil.setJdbcTemplate(key.toString(), jdbcTemplate);
                    log.info(String.format("检查到数据源:%s,放入JdbcTemplateUtil工具类中", key.toString()));
                }
            } else {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                JdbcTemplateUtil.setJdbcTemplate(name, jdbcTemplate);
                log.info(String.format("检查到数据源:%s,放入JdbcTemplateUtil工具类中", name));
            }
            //设置默认
            JdbcTemplateUtil.setDefaultJdbcTemplate(new JdbcTemplate(dataSource));
        }

    }
}
