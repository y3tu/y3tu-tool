package com.y3tu.tool.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.sql.DataSource;


import com.y3tu.tool.core.lang.VoidFunc;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.dialect.Dialect;
import com.y3tu.tool.db.dialect.DialectFactory;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.db.sql.Wrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库SQL执行会话<br>
 * 会话通过共用Connection而可以实现JDBC事务<br>
 * 一个会话只维护一个连接，推荐在执行完后关闭Session，避免重用<br>
 * 本对象并不是线程安全的，多个线程共用一个Session将会导致不可预知的问题
 * 
 * @author loolly
 *
 */
@Slf4j
public class Session extends AbstractDb implements Closeable {

	/**
	 * 创建默认数据源会话
	 * 
	 * @return {@link Session}
	 *
	 */
	public static Session create() {
		return new Session(DSFactory.get());
	}
	
	/**
	 * 创建会话
	 * 
	 * @param group 分组
	 * @return {@link Session}
	 * StringUtils
	 */
	public static Session create(String group) {
		return new Session(DSFactory.get(group));
	}

	/**
	 * 创建会话
	 * 
	 * @param ds 数据源
	 * @return {@link Session}
	 */
	public static Session create(DataSource ds) {
		return new Session(ds);
	}

	// ---------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，从DataSource中识别方言
	 * 
	 * @param ds 数据源
	 */
	public Session(DataSource ds) {
		this(ds, DialectFactory.getDialect(ds));
	}
	
	/**
	 * 构造
	 * 
	 * @param ds 数据源
	 * @param driverClassName 数据库连接驱动类名，用于识别方言
	 */
	public Session(DataSource ds, String driverClassName) {
		this(ds, DialectFactory.newDialect(driverClassName));
	}

	/**
	 * 构造
	 * 
	 * @param ds 数据源
	 * @param dialect 方言
	 */
	public Session(DataSource ds, Dialect dialect) {
		super(ds, dialect);
	}
	// ---------------------------------------------------------------------------- Constructor end

	// ---------------------------------------------------------------------------- Getters and Setters end
	/**
	 * 获得{@link SqlConnRunner}
	 * 
	 * @return {@link SqlConnRunner}
	 */
	@Override
	public SqlConnRunner getRunner() {
		return runner;
	}
	// ---------------------------------------------------------------------------- Getters and Setters end

	// ---------------------------------------------------------------------------- Transaction method start
	/**
	 * 开始事务
	 * 
	 * @throws SQLException SQL执行异常
	 */
	public void beginTransaction() throws SQLException {
		final Connection conn = getConnection();
		checkTransactionSupported(conn);
		conn.setAutoCommit(false);
	}

	/**
	 * 提交事务
	 * 
	 * @throws SQLException SQL执行异常
	 */
	public void commit() throws SQLException {
		try {
			getConnection().commit();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 回滚事务
	 * 
	 * @throws SQLException SQL执行异常
	 */
	public void rollback() throws SQLException {
		try {
			getConnection().rollback();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 静默回滚事务<br>
	 * 回滚事务
	 */
	public void quietRollback() {
		try {
			getConnection().rollback();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 回滚到某个保存点，保存点的设置请使用setSavepoint方法
	 * 
	 * @param savepoint 保存点
	 * @throws SQLException SQL执行异常
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		try {
			getConnection().rollback(savepoint);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 静默回滚到某个保存点，保存点的设置请使用setSavepoint方法
	 * 
	 * @param savepoint 保存点
	 * @throws SQLException SQL执行异常
	 */
	public void quietRollback(Savepoint savepoint) throws SQLException {
		try {
			getConnection().rollback(savepoint);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				getConnection().setAutoCommit(true); // 事务结束，恢复自动提交
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 设置保存点
	 * 
	 * @return 保存点对象
	 * @throws SQLException SQL执行异常
	 */
	public Savepoint setSavepoint() throws SQLException {
		return getConnection().setSavepoint();
	}

	/**
	 * 设置保存点
	 * 
	 * @param name 保存点的名称
	 * @return 保存点对象
	 * @throws SQLException SQL执行异常
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		return getConnection().setSavepoint(name);
	}

	/**
	 * 设置事务的隔离级别<br>
	 * 
	 * Connection.TRANSACTION_NONE 驱动不支持事务<br>
	 * Connection.TRANSACTION_READ_UNCOMMITTED 允许脏读、不可重复读和幻读<br>
	 * Connection.TRANSACTION_READ_COMMITTED 禁止脏读，但允许不可重复读和幻读<br>
	 * Connection.TRANSACTION_REPEATABLE_READ 禁止脏读和不可重复读，单运行幻读<br>
	 * Connection.TRANSACTION_SERIALIZABLE 禁止脏读、不可重复读和幻读<br>
	 * 
	 * @param level 隔离级别
	 * @throws SQLException SQL执行异常
	 */
	public void setTransactionIsolation(int level) throws SQLException {
		if (getConnection().getMetaData().supportsTransactionIsolationLevel(level) == false) {
			throw new SQLException(StringUtils.format("Transaction isolation [{}] not support!", level));
		}
		getConnection().setTransactionIsolation(level);
	}

	/**
	 * 在事务中执行操作，通过实现{@link VoidFunc}接口的call方法执行多条SQL语句从而完成事务
	 * 
	 * @param func 函数抽象，在函数中执行多个SQL操作，多个操作会被合并为同一事务
	 *
	 */
	public void trans(VoidFunc func) {
		try {
			beginTransaction();
			func.call();
			commit();
		} catch (Exception e) {
			quietRollback();
			throw new DbRuntimeException(e);
		}
	}
	// ---------------------------------------------------------------------------- Transaction method end

	// ---------------------------------------------------------------------------- Getters and Setters start
	@Override
	public Session setWrapper(Character wrapperChar) {
		return (Session) super.setWrapper(wrapperChar);
	}

	@Override
	public Session setWrapper(Wrapper wrapper) {
		return (Session) super.setWrapper(wrapper);
	}
	// ---------------------------------------------------------------------------- Getters and Setters end

	@Override
	public Connection getConnection() throws SQLException {
		return ThreadLocalConnection.INSTANCE.get(this.ds);
	}

	@Override
	public void closeConnection(Connection conn) {
		ThreadLocalConnection.INSTANCE.close(this.ds);
	}

	@Override
	public void close() {
		closeConnection(null);
	}
}
