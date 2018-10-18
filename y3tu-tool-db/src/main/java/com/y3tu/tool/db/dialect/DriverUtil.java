package com.y3tu.tool.db.dialect;

import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.DbRuntimeException;
import com.y3tu.tool.db.DbUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 驱动相关工具类，包括自动获取驱动类名
 *
 * @author looly
 */
public class DriverUtil {
    /**
     * 通过JDBC URL等信息识别JDBC驱动名
     *
     * @param nameContainsProductInfo 包含数据库标识的字符串
     * @return 驱动
     * @see DialectFactory#identifyDriver(String)
     */
    public static String identifyDriver(String nameContainsProductInfo) {
        return DialectFactory.identifyDriver(nameContainsProductInfo);
    }

    /**
     * 识别JDBC驱动名
     *
     * @param ds 数据源
     * @return 驱动
     */
    public static String identifyDriver(DataSource ds) {
        Connection conn = null;
        String driver = null;
        try {
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                throw new DbRuntimeException("Get Connection error !", e);
            } catch (NullPointerException e) {
                throw new DbRuntimeException("Unexpected NullPointException, maybe [jdbcUrl] or [url] is empty!", e);
            }
            driver = identifyDriver(conn);
        } finally {
            DbUtil.close(conn);
        }

        return driver;
    }

    /**
     * 识别JDBC驱动名
     *
     * @param conn 数据库连接对象
     * @return 驱动
     * @throws DbRuntimeException SQL异常包装，获取元数据信息失败
     */
    public static String identifyDriver(Connection conn) throws DbRuntimeException {
        String driver = null;
        try {
            DatabaseMetaData meta = conn.getMetaData();
            driver = identifyDriver(meta.getDatabaseProductName());
            if (StringUtils.isBlank(driver)) {
                driver = identifyDriver(meta.getDriverName());
            }
        } catch (SQLException e) {
            throw new DbRuntimeException("Identify driver error!", e);
        }

        return driver;
    }
}
