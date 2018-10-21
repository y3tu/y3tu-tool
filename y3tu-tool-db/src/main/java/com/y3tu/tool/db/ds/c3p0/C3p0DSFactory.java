package com.y3tu.tool.db.ds.c3p0;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.DbRuntimeException;
import com.y3tu.tool.db.dialect.DriverUtil;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.setting.Setting;


/**
 * Druid数据源工厂类
 *
 * @author Looly
 */
public class C3p0DSFactory extends DSFactory {

    public static final String DS_NAME = "C3P0";

    /**
     * 数据源池
     */
    private Map<String, ComboPooledDataSource> dsMap;

    public C3p0DSFactory() {
        this(null);
    }

    public C3p0DSFactory(Setting setting) {
        super(DS_NAME, ComboPooledDataSource.class, setting);
        this.dsMap = new ConcurrentHashMap<>();
    }

    @Override
    synchronized public DataSource getDataSource(String group) {
        if (group == null) {
            group = StringUtils.EMPTY;
        }

        // 如果已经存在已有数据源（连接池）直接返回
        final ComboPooledDataSource existedDataSource = dsMap.get(group);
        if (existedDataSource != null) {
            return existedDataSource;
        }

        ComboPooledDataSource ds = createDataSource(group);
        // 添加到数据源池中，以备下次使用
        dsMap.put(group, ds);
        return ds;
    }

    @Override
    public void close(String group) {
        if (group == null) {
            group = StringUtils.EMPTY;
        }

        ComboPooledDataSource ds = dsMap.get(group);
        if (ds != null) {
            ds.close();
            dsMap.remove(group);
        }
    }

    @Override
    public void destroy() {
        if (MapUtil.isNotEmpty(dsMap)) {
            Collection<ComboPooledDataSource> values = dsMap.values();
            for (ComboPooledDataSource ds : values) {
                ds.close();
            }
            dsMap.clear();
        }
    }

    /**
     * 创建数据源
     *
     * @param group 分组
     * @return C3P0数据源 {@link ComboPooledDataSource}
     */
    private ComboPooledDataSource createDataSource(String group) {
        final Setting config = setting.getSetting(group);
        if (MapUtil.isEmpty(config)) {
            throw new DbRuntimeException("No C3P0 config for group: [{}]", group);
        }

        final ComboPooledDataSource ds = new ComboPooledDataSource();

        // 基本信息
        final String url = config.getAndRemoveStr(KEY_ALIAS_URL);
        if (StringUtils.isBlank(url)) {
            throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
        }
        ds.setJdbcUrl(url);
        ds.setUser(config.getAndRemoveStr(KEY_ALIAS_USER));
        ds.setPassword(config.getAndRemoveStr(KEY_ALIAS_PASSWORD));
        final String driver = config.getAndRemoveStr(KEY_ALIAS_DRIVER);
        try {
            if (StringUtils.isNotBlank(driver)) {
                ds.setDriverClass(driver);
            } else {
                ds.setDriverClass(DriverUtil.identifyDriver(url));
            }
        } catch (Exception e) {
            throw new DbRuntimeException(e);
        }

        // 注入属性
        config.toBean(ds, group, ComboPooledDataSource.class);

        return ds;
    }
}