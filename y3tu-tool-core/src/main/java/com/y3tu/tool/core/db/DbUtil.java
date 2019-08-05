package com.y3tu.tool.core.db;

import cn.hutool.core.convert.Convert;
import cn.hutool.db.Db;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.Session;
import cn.hutool.db.SqlConnRunner;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.sql.SqlLog;
import cn.hutool.log.level.Level;
import cn.hutool.setting.Setting;
import com.y3tu.tool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.*;

/**
 * 数据库操作工具类
 * @author y3tu
 * @date 2019-08-02
 *
 * @see cn.hutool.db.DbUtil
 */
@Slf4j
public class DbUtil  {
    /**
     * 实例化一个新的SQL运行对象
     *
     * @param dialect 数据源
     * @return SQL执行类
     */
    public static SqlConnRunner newSqlConnRunner(Dialect dialect) {
        return SqlConnRunner.create(dialect);
    }

    /**
     * 实例化一个新的SQL运行对象
     *
     * @param ds 数据源
     * @return SQL执行类
     */
    public static SqlConnRunner newSqlConnRunner(DataSource ds) {
        return SqlConnRunner.create(ds);
    }

    /**
     * 实例化一个新的SQL运行对象
     *
     * @param conn 数据库连接对象
     * @return SQL执行类
     */
    public static SqlConnRunner newSqlConnRunner(Connection conn) {
        return SqlConnRunner.create(DialectFactory.newDialect(conn));
    }
    /**
     * 实例化一个新的Db，使用默认数据源
     *
     * @return SQL执行类
     */
    public static Db use() {
        return Db.use();
    }

    /**
     * 实例化一个新的Db对象
     *
     * @param ds 数据源
     * @return SQL执行类
     */
    public static Db use(DataSource ds) {
        return Db.use(ds);
    }

    /**
     * 实例化一个新的SQL运行对象
     *
     * @param ds      数据源
     * @param dialect SQL方言
     * @return SQL执行类
     */
    public static Db use(DataSource ds, Dialect dialect) {
        return Db.use(ds, dialect);
    }

    /**
     * 新建数据库会话，使用默认数据源
     *
     * @return 数据库会话
     */
    public static Session newSession() {
        return Session.create(getDs());
    }

    /**
     * 新建数据库会话
     *
     * @param ds 数据源
     * @return 数据库会话
     */
    public static Session newSession(DataSource ds) {
        return Session.create(ds);
    }

    /**
     * 连续关闭一系列的SQL相关对象<br>
     * 这些对象必须按照顺序关闭，否则会出错。
     *
     * @param objsToClose 需要关闭的对象
     */
    public static void close(Object... objsToClose) {
        for (Object obj : objsToClose) {
            if (obj instanceof AutoCloseable) {
                IoUtil.close((AutoCloseable) obj);
            } else if (obj instanceof Closeable) {
                IoUtil.close((Closeable) obj);
            } else {
                try {
                    if (obj != null) {
                        if (obj instanceof ResultSet) {
                            ((ResultSet) obj).close();
                        } else if (obj instanceof Statement) {
                            ((Statement) obj).close();
                        } else if (obj instanceof PreparedStatement) {
                            ((PreparedStatement) obj).close();
                        } else if (obj instanceof Connection) {
                            ((Connection) obj).close();
                        } else {
                            log.warn("Object {} not a ResultSet or Statement or PreparedStatement or Connection!", obj.getClass().getName());
                        }
                    }
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * 获得默认数据源
     *
     * @return 默认数据源
     */
    public static DataSource getDs() {
        return DSFactory.get();
    }

    /**
     * 获取指定分组的数据源
     *
     * @param group 分组
     * @return 数据源
     */
    public static DataSource getDs(String group) {
        return DSFactory.get(group);
    }

    /**
     * 获得JNDI数据源
     *
     * @param jndiName JNDI名称
     * @return 数据源
     */
    public static DataSource getJndiDsWithLog(String jndiName) {
        try {
            return getJndiDs(jndiName);
        } catch (DbRuntimeException e) {
            log.error("Find JNDI datasource error!", e.getCause());
        }
        return null;
    }

    /**
     * 获得JNDI数据源
     *
     * @param jndiName JNDI名称
     * @return 数据源
     */
    public static DataSource getJndiDs(String jndiName) {
        try {
            return (DataSource) new InitialContext().lookup(jndiName);
        } catch (NamingException e) {
            throw new DbRuntimeException(e);
        }
    }

    /**
     * 从配置文件中读取SQL打印选项
     *
     * @param setting 配置文件
     */
    public static void setShowSqlGlobal(Setting setting) {
        // 初始化SQL显示
        final boolean isShowSql = Convert.toBool(setting.remove("showSql"), false);
        final boolean isFormatSql = Convert.toBool(setting.remove("formatSql"), false);
        final boolean isShowParams = Convert.toBool(setting.remove("showParams"), false);
        String sqlLevelStr = setting.remove("sqlLevel");
        if (null != sqlLevelStr) {
            sqlLevelStr = sqlLevelStr.toUpperCase();
        }
        final Level level = Convert.toEnum(Level.class, sqlLevelStr, Level.DEBUG);
        log.debug("Show sql: [{}], format sql: [{}], show params: [{}], level: [{}]", isShowSql, isFormatSql, isShowParams, level);
        setShowSqlGlobal(isShowSql, isFormatSql, isShowParams, level);
    }

    /**
     * 设置全局配置：是否通过debug日志显示SQL
     *
     * @param isShowSql    是否显示SQL
     * @param isFormatSql  是否格式化显示的SQL
     * @param isShowParams 是否打印参数
     */
    public static void setShowSqlGlobal(boolean isShowSql, boolean isFormatSql, boolean isShowParams, Level level) {
        SqlLog.INSTASNCE.init(isShowSql, isFormatSql, isShowParams,level);
    }
}
