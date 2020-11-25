package com.y3tu.tool.web.sql;

import com.y3tu.tool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 封装常用sql操作
 *
 * @author y3tu
 */
@Slf4j
public class SqlUtil {


    /**
     * 多线程处理大数据分页查询操作
     * <p>
     * 注意 sql居中需要包含$MOD('取模字段',?)=?
     *
     * @param size         线程数
     * @param selectSql    查询数据语句
     * @param selectDsName 查询数据数据源名称
     * @param params       查询参数 格式为 key:1 value:"servId"； key值代表查询sql语句中?占位符的位置
     * @param dataHandler  对查询结果进行处理
     * @return true: 正常处理 false:处理失败或某个线程出现异常
     * @throws Exception
     */
    public static boolean dataHandler(int size, String selectSql, String selectDsName, Map<Integer, Object> params, DataHandler dataHandler) throws Exception {

        if (!selectSql.toUpperCase().contains("$MOD")) {
            throw new Exception("多线程处理查询数据sql语句必须包含$MOD");
        }

        CountDownLatch cdl1 = ThreadUtil.newCountDownLatch(size);

        ExecutorService executor = ThreadUtil.newFixedExecutor(size, "dataHandler多线程");
        Map<String, ThreadResult> threadResultMap = new HashMap<>();

        for (int i = 0; i < size; i++) {
            executor.execute(new DataPageHandlerRunnable(selectSql, selectDsName, params, dataHandler, size, i, cdl1, threadResultMap));
        }
        while (true) {
            if (cdl1.getCount() == 0) {
                log.info("------线程执行完成------");
                break;
            }
        }
        executor.shutdownNow();

        List<Boolean> resultList = threadResultMap.keySet().stream().map(key -> threadResultMap.get(key).isSuccess()).collect(Collectors.toList());

        if (resultList.contains(false)) {
            return false;
        }
        return true;
    }

    /**
     * 单线程处理数据分页查询操作
     *
     * @param selectSql    查询数据语句
     * @param selectDsName 查询数据数据源名称
     * @param params       查询参数 格式为 key:1 value:"servId"； key值代表查询sql语句中?占位符的位置
     * @param dataHandler  对查询结果进行处理
     * @return true: 正常处理 false:处理失败或某个线程出现异常
     * @throws Exception
     */
    public boolean dataHandler(String selectSql, String selectDsName, Map<Integer, Object> params, DataHandler dataHandler) throws Exception {
        Map<String, ThreadResult> threadResultMap = new HashMap<>();
        ThreadUtil.execute(new DataPageHandlerRunnable(selectSql, selectDsName, params, dataHandler, 1, 1, null, threadResultMap));
        List<Boolean> resultList = threadResultMap.keySet().stream().map(key -> threadResultMap.get(key).isSuccess()).collect(Collectors.toList());

        if (resultList.contains(false)) {
            return false;
        }
        return true;
    }


    /**
     * 大数据插入更新操作
     *
     * @param sql      插入sql
     * @param dsName   插入数据源名称
     * @param dataList 待插入或者更新的数据
     * @return
     */
    public static int batchUpdate(String sql, String dsName, List<Map<String, Object>> dataList) {
        List<Object[]> argsList = new ArrayList<>();
        for (Map<String, Object> dto : dataList) {
            List<Object> insertObjList = new ArrayList<>();
            for (String key : dto.keySet()) {
                insertObjList.add(dto.get(key));
            }
            argsList.add(insertObjList.toArray());
        }
        return JdbcTemplateUtil.getJdbcTemplate(dsName).batchUpdate(sql, argsList).length;
    }


    /**
     * 清空表数据
     *
     * @param tableName 表名
     * @param dsName    表数据源名
     */
    public static void truncate(String tableName, String dsName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" truncate table ").append(tableName);
        log.info("执行SQL语句:" + sql.toString());
        JdbcTemplateUtil.getJdbcTemplate(dsName).execute(sql.toString());
    }

    /**
     * 更新sql语句
     *
     * @param sql    sql语句
     * @param dsName 数据源名
     * @return
     */
    public static int update(String sql, String dsName) {
        log.info("执行SQL语句:" + sql);
        return JdbcTemplateUtil.getJdbcTemplate(dsName).update(sql);
    }

    /**
     * 执行sql语句
     *
     * @param sql    sql语句
     * @param dsName 数据源名
     */
    public static void execute(String sql, String dsName) {
        log.info("执行SQL语句:" + sql);
        JdbcTemplateUtil.getJdbcTemplate(dsName).execute(sql);
    }


    /**
     * 计算数据量
     *
     * @param sql
     * @param dsName
     * @return
     */
    public static int count(String sql, String dsName) {
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) as count from (").append(sql).append(") countTable ");
        Map<String, Object> data = JdbcTemplateUtil.getJdbcTemplate(dsName).queryForMap(countSql.toString());
        log.info(countSql.toString());
        if (data.get("count") != null) {
            int count = Integer.parseInt(data.get("count").toString());
            return count;
        }
        return 0;
    }


}
