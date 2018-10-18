package com.y3tu.tool.db.dialect.impl;


import com.y3tu.tool.db.dialect.DialectName;
import com.y3tu.tool.db.sql.Wrapper;

/**
 * Postgree方言
 *
 * @author loolly
 */
public class PostgresqlDialect extends AnsiSqlDialect {
    public PostgresqlDialect() {
        wrapper = new Wrapper('"');
    }

    @Override
    public DialectName dialectName() {
        return DialectName.POSTGREESQL;
    }
}
