package com.y3tu.tool.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/5
 */
public class SqlStrUtilTest {

    @Test
    public void splitSqlByPage() {
        StringUtils.out(SqlStrUtil.splitSqlByPage("select * from report",10,1,SqlStrUtil.SqlType.MYSQL));
    }
}