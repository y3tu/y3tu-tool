package com.y3tu.tool.db;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.y3tu.tool.core.collection.SetUtil;
import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.util.EnumUtil;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.db.ds.druid.DruidDSFactory;
import com.y3tu.tool.db.meta.DataTypeEnum;
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
        DSFactory.setCurrentDSFactory(new DruidDSFactory());
        DataSource ds = DSFactory.get("mysql");


        Table table = MetaUtil.getTableMeta(ds, "sys_user");

        String javaType = DataTypeEnum.getJavaType(table.get("user_id").getTypeName());

        String[] columnType = MetaUtil.getColumnNames(ds,"sys_user");
        Arrays.stream(columnType).forEach(type->{
            Console.log(DataTypeEnum.getJavaType(table.get(type).getTypeName()));
        });
        Assert.assertEquals(SetUtil.newHashSet("user_id"), table.getPkNames());
    }


}
