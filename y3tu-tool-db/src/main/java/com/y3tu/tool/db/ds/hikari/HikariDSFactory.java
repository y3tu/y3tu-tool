package com.y3tu.tool.db.ds.hikari;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.DbRuntimeException;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.setting.Setting;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


/**
 * HikariCP数据源工厂类
 *
 * @author Looly
 */
public class HikariDSFactory extends DSFactory {

    public static final String DS_NAME = "HikariCP";

    /**
     * 数据源池
     */
    private Map<String, HikariDataSource> dsMap;

    public HikariDSFactory() {
        this(null);
    }

    public HikariDSFactory(Setting setting) {
        super(DS_NAME, HikariDataSource.class, setting);
        this.dsMap = new ConcurrentHashMap<>();
    }

    @Override
    synchronized public DataSource getDataSource(String group) {
        if (group == null) {
            group = StringUtils.EMPTY;
        }

        // 如果已经存在已有数据源（连接池）直接返回
        final HikariDataSource existedDataSource = dsMap.get(group);
        if (existedDataSource != null) {
            return existedDataSource;
        }

        final HikariDataSource ds = createDataSource(group);
        // 添加到数据源池中，以备下次使用
        dsMap.put(group, ds);
        return ds;
    }

    @Override
    public void close(String group) {
        if (group == null) {
            group = StringUtils.EMPTY;
        }

        HikariDataSource ds = dsMap.get(group);
        if (ds != null) {
            IOUtil.close(ds);
            dsMap.remove(group);
        }
    }

    @Override
    public void destroy() {
        if (MapUtil.isNotEmpty(dsMap)) {
            Collection<HikariDataSource> values = dsMap.values();
            for (HikariDataSource ds : values) {
                IOUtil.close(ds);
            }
            dsMap.clear();
        }
    }

    /**
     * 创建数据源
     *
     * @param group 分组
     * @return Hikari数据源 {@link HikariDataSource}
     */
    private HikariDataSource createDataSource(String group) {
        if (group == null) {
            group = StringUtils.EMPTY;
        }

        final Properties config = setting.getProperties(group);
        if (MapUtil.isEmpty(config)) {
            throw new DbRuntimeException("No HikariCP config for group: [{}]", group);
        }

        // 规范化属性名
        if (false == config.containsKey("jdbcUrl") && config.containsKey("url")) {
            config.put("jdbcUrl", config.remove("url"));
        }
        if (false == config.containsKey("username") && config.containsKey("user")) {
            config.put("username", config.remove("user"));
        }
        if (false == config.containsKey("password") && config.containsKey("pass")) {
            config.put("password", config.remove("pass"));
        }
        if (false == config.containsKey("driverClassName") && config.containsKey("driver")) {
            config.put("driverClassName", config.remove("driver"));
        }

        final HikariDataSource ds = new HikariDataSource(new HikariConfig(config));
        return ds;
    }
}
