package com.y3tu.tool.db.handler;

import com.y3tu.tool.db.Entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Entity对象处理器，只处理第一条数据
 *
 * @author loolly
 */
public class EntityHandler implements RsHandler<Entity> {

    /**
     * 创建一个 EntityHandler对象
     *
     * @return EntityHandler对象
     */
    public static EntityHandler create() {
        return new EntityHandler();
    }

    @Override
    public Entity handle(ResultSet rs) throws SQLException {
        final ResultSetMetaData meta = rs.getMetaData();
        final int columnCount = meta.getColumnCount();

        return rs.next() ? HandleHelper.handleRow(columnCount, meta, rs) : null;
    }
}
