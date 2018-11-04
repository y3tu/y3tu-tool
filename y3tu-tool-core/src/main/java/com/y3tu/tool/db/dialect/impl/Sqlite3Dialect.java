package com.y3tu.tool.db.dialect.impl;

import com.y3tu.tool.db.dialect.DialectName;
import com.y3tu.tool.db.sql.Wrapper;

/**
 * SqlLite3方言
 *
 * @author loolly
 */
public class Sqlite3Dialect extends AnsiSqlDialect {
    public Sqlite3Dialect() {
        wrapper = new Wrapper('[', ']');
    }

    @Override
    public DialectName dialectName() {
        return DialectName.SQLITE3;
    }
}
