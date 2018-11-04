package com.y3tu.tool.db.sql;


import lombok.extern.slf4j.Slf4j;

/**
 * SQL在日志中打印配置
 *
 * @author looly
 */
@Slf4j
public enum SqlLog {
    INSTASNCE;


    /**
     * 是否debugSQL
     */
    private boolean showSql;
    /**
     * 是否格式化SQL
     */
    private boolean formatSql;
    /**
     * 是否显示参数
     */
    private boolean showParams;
    /** 默认日志级别 */
    //private Level level = Level.DEBUG;

    /**
     * 设置全局配置：是否通过debug日志显示SQL
     *
     * @param isShowSql    是否显示SQL
     * @param isFormatSql  是否格式化显示的SQL
     * @param isShowParams 是否打印参数
     */
    public void init(boolean isShowSql, boolean isFormatSql, boolean isShowParams) {
        this.showSql = isShowSql;
        this.formatSql = isFormatSql;
        this.showParams = isShowParams;
        //this.level = level;
    }

    /**
     * 打印SQL日志
     *
     * @param sql         SQL语句
     * @param paramValues 参数，可为null
     */
    public void log(String sql, Object paramValues) {
        if (this.showSql) {
            if (this.showParams) {
                log.info("\nSQL -> {}\nParams -> {}", this.formatSql ? SqlFormatter.format(sql) : sql, paramValues);
            } else {
                log.debug("\nSQL -> {}", this.formatSql ? SqlFormatter.format(sql) : sql);
            }
        }
    }
}
