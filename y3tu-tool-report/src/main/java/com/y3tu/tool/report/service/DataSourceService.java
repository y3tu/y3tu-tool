package com.y3tu.tool.report.service;

import com.y3tu.tool.report.domain.DataSource;
import com.y3tu.tool.web.base.jpa.BaseService;

/**
 * @author y3tu
 */
public interface DataSourceService extends BaseService<DataSource> {

    /**
     * 测试数据源连接
     *
     * @param dataSource 数据源配置
     * @return
     */
    boolean testConnection(DataSource dataSource);


}
