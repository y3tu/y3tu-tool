package com.y3tu.tool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理接口<br>
 * 默认实现有：

 * 
 * @author Luxiaolei
 *
 */
public interface RsHandler<T> {
	
	/**
	 * 处理结果集<br>
	 * 结果集处理后不需要关闭
	 * @param rs 结果集
	 * @return 处理后生成的对象
	 * @throws SQLException SQL异常
	 */
	public T handle(ResultSet rs) throws SQLException;
}
