package com.y3tu.tool.test.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.sql.DataHandler;
import com.y3tu.tool.web.sql.SqlUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("sql")
public class SqlController {
    /**
     * 多线程跨库操作数据
     *
     * @return
     */
    @GetMapping("batchInsert")
    public R testSqlUtil() {
        String selectSql = "select id,name,age,email from user where $MOD(id,?)=?";
        SqlUtil.dataPageHandler(30, 300, selectSql, "support", new DataHandler() {
            @Override
            public void handle(List dataList) {
                //batchUpdate此时是不支持事务控制的
                SqlUtil.batchUpdate("insert into user(id,name,age,email)values(?,?,?,?)", "report", dataList);
            }
        });
        return R.success();
    }
}
