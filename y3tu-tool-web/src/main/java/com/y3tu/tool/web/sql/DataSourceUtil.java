package com.y3tu.tool.web.sql;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源工具类
 *
 * @author y3tu
 */
public class DataSourceUtil {

    /**
     * 默认JdbcTemplate
     */
    protected JdbcTemplate defaultJdbcTemplate;

    /**
     * JdbcTemplate容器
     */
    private static ConcurrentHashMap<String, JdbcTemplate> dataSourceContainer = new ConcurrentHashMap<>();


    public JdbcTemplate getDbService() {
        return defaultJdbcTemplate;
    }

    public JdbcTemplate getDbService(String name) {
        return dataSourceContainer.getOrDefault(name, defaultJdbcTemplate);
    }


}
