package com.y3tu.tool.test.rest;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.excel.ExcelUtil;
import com.y3tu.tool.web.sql.DataHandler;
import com.y3tu.tool.web.sql.SqlUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 简单测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("sql")
    public R testSqlUtil() {
        String selectSql = "select id,name,age,email from user where $MOD(id,?)=?";
        SqlUtil.dataPageHandler(30, 300, selectSql, "support", null, new DataHandler() {
            @Override
            public void handle(List dataList) {
                //batchUpdate此时是不支持事务控制的
                SqlUtil.batchUpdate("insert into user(id,name,age,email)values(?,?,?,?)", "report", dataList);
            }
        });

        return R.success();
    }

    @GetMapping("excel")
    public void testExportExcel(HttpServletResponse response) throws Exception {

        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("NAME");
        includeColumnFiledNames.add("AGE");

        String selectSql = "select id,name,age,email from user";

        SqlUtil.dataPageHandler(true, 1000000, selectSql, "support", null, new DataHandler() {
            @Override
            public void handle(List dataList) {
                ExcelUtil.downExcel("测试", "测试", dataList, Map.class, includeColumnFiledNames, ExcelTypeEnum.XLSX, response);
            }
        });
    }
}
