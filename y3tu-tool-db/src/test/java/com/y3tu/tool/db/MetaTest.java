package com.y3tu.tool.db;

import java.util.List;

import javax.sql.DataSource;

import com.y3tu.tool.core.collection.SetUtil;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.db.meta.MetaUtil;
import com.y3tu.tool.db.meta.Table;
import org.junit.Assert;
import org.junit.Test;


/**
 * 元数据信息单元测试
 *
 * @author Looly
 */
public class MetaTest {
    DataSource ds = DSFactory.get("mysql");

    @Test
    public void getTablesTest() {
        List<String> tables = MetaUtil.getTables(ds);
        Assert.assertTrue(tables.contains("sys_user"));
    }

    @Test
    public void getTableMetaTest() {

        Table table = MetaUtil.getTableMeta(ds, "sys_user");
        Assert.assertEquals(SetUtil.newHashSet("user_id"), table.getPkNames());
    }

}
