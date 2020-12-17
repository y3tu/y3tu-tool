package com.y3tu.tool.test.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.sql.DataHandler;
import com.y3tu.tool.web.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据库测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("sql")
@Slf4j
public class SqlController {
    /**
     * 多线程跨库操作数据
     *
     * @return
     */
    @GetMapping("batchInsert")
    public R batchInsert() {
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

    @GetMapping("query")
    public R query() {
        String sql = "select real_name from local_storage";
        List<Map<String, Object>> list = SqlUtil.queryList(sql, null, "support");
        for (Map<String, Object> map : list) {
            String realName = map.get("real_name").toString();
            log.info(realName);
        }
        return R.success(list);
    }
}
