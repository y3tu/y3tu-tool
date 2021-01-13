package com.y3tu.tool.report.util;

import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.DataSourceWrapper;
import cn.hutool.setting.Setting;
import com.y3tu.tool.report.entity.domain.DataSource;
import com.y3tu.tool.report.service.DataSourceService;
import com.y3tu.tool.web.util.SpringContextUtil;

/**
 * 数据源工具类
 *
 * @author y3tu
 */
public class DataSourceUtil {

    /**
     * 根据数据源ID获取数据源
     *
     * @param dsId
     * @return
     */
    public static javax.sql.DataSource getDataSourceByDsId(int dsId) {
        DataSourceService dataSourceService = (DataSourceService) SpringContextUtil.getBean(DataSourceService.class);
        DataSource dataSource = (DataSource) dataSourceService.getById(dsId);
        return getDataSource(dataSource);
    }


    /**
     * 根据数据源配置获取数据源
     *
     * @param dataSource
     * @return
     */
    public static javax.sql.DataSource getDataSource(DataSource dataSource) {

        Setting setting = new Setting();

        setting.set("url", dataSource.getDbUrl())
                .set("driver", dataSource.getDbDriver())
                .set("user", dataSource.getDbUsername())
                .set("pass", dataSource.getDbPassword());

        DSFactory dsFactory = DSFactory.create(setting);
        DataSourceWrapper dataSourceWrapper = (DataSourceWrapper) dsFactory.getDataSource();
        javax.sql.DataSource ds = dataSourceWrapper.getRaw();
        return ds;
    }
}
