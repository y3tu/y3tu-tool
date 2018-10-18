package com.y3tu.tool.db.dialect.impl;


import com.y3tu.tool.db.Page;
import com.y3tu.tool.db.dialect.DialectName;
import com.y3tu.tool.db.sql.SqlBuilder;
import com.y3tu.tool.db.sql.Wrapper;

/**
 * MySQL方言
 *
 * @author loolly
 */
public class MysqlDialect extends AnsiSqlDialect {

    public MysqlDialect() {
        wrapper = new Wrapper('`');
    }

    @Override
    protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
        return find.append(" LIMIT ").append(page.getStartPosition()).append(", ").append(page.getPageSize());
    }

    @Override
    public DialectName dialectName() {
        return DialectName.MYSQL;
    }
}
