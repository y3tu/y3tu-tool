package com.y3tu.tool.db.transaction;

import com.y3tu.tool.db.Db;
import com.y3tu.tool.db.DbRuntimeException;

import java.sql.SQLException;

/**
 * 事务函数
 *
 * @author looly
 */
public interface TxFunc {

    /**
     * 执行具体的事务，通过db参数调用多个数据库操作方法，这些方法会被做为一个整体事务提交或者回滚。<br>
     * 如果执行结果不满足要求而需要回滚，请抛出一个异常 (例如{@link DbRuntimeException})
     *
     * @param db Db对象
     */
    void call(Db db) throws SQLException;
}
