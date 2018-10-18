package com.y3tu.tool.db.ds.pooled;


import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.DbRuntimeException;
import com.y3tu.tool.db.dialect.DriverUtil;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.setting.Setting;

import static com.y3tu.tool.core.convert.ConvertUtil.toInt;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

/**
 * 数据库配置文件类，此类对应一个数据库配置文件
 *
 * @author Looly
 */
public class DbSetting {
    /**
     * 默认的数据库连接配置文件路径
     */
    public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";

    private Setting setting;

    /**
     * 构造
     */
    public DbSetting() {
        this(null);
    }

    /**
     * 构造
     *
     * @param setting 数据库配置
     */
    public DbSetting(Setting setting) {
        if (null == setting) {
            this.setting = new Setting(DEFAULT_DB_CONFIG_PATH);
        } else {
            this.setting = setting;
        }
    }

    /**
     * 获得数据库连接信息
     *
     * @param group 分组
     * @return 分组
     */
    public DbConfig getDbConfig(String group) {
        final Setting config = setting.getSetting(group);
        if (MapUtil.isEmpty(config)) {
            throw new DbRuntimeException("No Hutool pool config for group: [{}]", group);
        }

        final DbConfig dbConfig = new DbConfig();

        // 基本信息
        final String url = config.getAndRemoveStr(DSFactory.KEY_ALIAS_URL);
        if (StringUtils.isBlank(url)) {
            throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
        }
        dbConfig.setUrl(url);
        dbConfig.setUser(config.getAndRemoveStr(DSFactory.KEY_ALIAS_USER));
        dbConfig.setPass(config.getAndRemoveStr(DSFactory.KEY_ALIAS_PASSWORD));
        final String driver = config.getAndRemoveStr(DSFactory.KEY_ALIAS_DRIVER);
        if (StringUtils.isNotBlank(driver)) {
            dbConfig.setDriver(driver);
        } else {
            dbConfig.setDriver(DriverUtil.identifyDriver(url));
        }

        // 连接池相关信息
        dbConfig.setInitialSize(toInt(setting.getByGroup("initialSize", group), 0));
        dbConfig.setMinIdle(toInt(setting.getByGroup("minIdle", group), 0));
        dbConfig.setMaxActive(toInt(setting.getByGroup("maxActive", group), 8));
        dbConfig.setMaxWait(toLong(setting.getByGroup("maxWait", group), 6000L));

        return dbConfig;
    }
}
