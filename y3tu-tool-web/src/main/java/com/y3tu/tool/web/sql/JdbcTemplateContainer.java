package com.y3tu.tool.web.sql;

import com.y3tu.tool.core.exception.ToolException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * JdbcTemplate容器
 *
 * @author y3tu
 */
public class JdbcTemplateContainer {

    /**
     * 默认JdbcTemplate
     */
    private static JdbcTemplate defaultJdbcTemplate;

    /**
     * JdbcTemplate容器
     */
    private static ConcurrentHashMap<String, JdbcTemplate> dataSourceContainer = new ConcurrentHashMap<>();


    /**
     * 获取默认的JdbcTemplate
     *
     * @return
     */
    public static JdbcTemplate getDefaultJdbcTemplate() {
        if (defaultJdbcTemplate != null) {
            return defaultJdbcTemplate;
        } else {
            List<JdbcTemplate> jdbcTemplateList = dataSourceContainer.keySet().stream().map(key -> dataSourceContainer.get(key)).collect(Collectors.toList());
            if (!jdbcTemplateList.isEmpty()) {
                defaultJdbcTemplate = jdbcTemplateList.get(0);
            } else {
                throw new ToolException("抱歉,没有检查到数据源！");
            }
        }
        return defaultJdbcTemplate;
    }

    public static void setDefaultJdbcTemplate(JdbcTemplate jdbcTemplate) {
        defaultJdbcTemplate = jdbcTemplate;
    }

    /**
     * 根据数据源在spring容器中的bean名称获取对应数据源的jdbcTemplate
     *
     * @param dataSourceBeanName 数据源的bean名称
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(String dataSourceBeanName) {
        return dataSourceContainer.getOrDefault(dataSourceBeanName, getDefaultJdbcTemplate());
    }

    public static void addJdbcTemplate(String dataSourceBeanName, JdbcTemplate jdbcTemplate) {
        dataSourceContainer.put(dataSourceBeanName, jdbcTemplate);
    }


}
