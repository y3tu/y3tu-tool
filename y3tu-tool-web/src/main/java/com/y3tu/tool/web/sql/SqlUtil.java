package com.y3tu.tool.web.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 封装常用sql操作
 *
 * @author y3tu
 */
@Component
@Slf4j
public class SqlUtil {

    /**
     * 单线程处理查询插入操作
     *
     * @param insertSql     插入语句
     * @param selectSql     查询数据语句
     * @param insertDsName  插入执行数据源名
     * @param selectDsName  查询数据数据源名
     * @param params        查询参数 格式为 key:1 value:"servId"； key值代表查询sql语句中?占位符的位置
     * @param selectHandler 对查询结果进行处理
     * @throws Exception
     */
    public void insertIntoSelect(String insertSql, String selectSql, String insertDsName, String selectDsName, Map<Integer, Object> params, SelectHandler selectHandler) throws Exception {
        CountDownLatch cdl1 = new CountDownLatch(1);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Map<String, ThreadRes> threadMap = new HashMap<String, ThreadRes>();

        executor.execute(new InsertIntoSelectRunnable(1, 0, cdl1, threadMap, insertSql, selectSql, insertDsName, selectDsName, params, dataUtil, selectHandler));

        while (true) {
            if (cdl1.getCount() == 0) {
                log.info("------入表线程执行完了------");
                break;
            }
        }
        executor.shutdownNow();
        JobUtil.getThreadsOne(1, threadMap);
    }

    /**
     * 多线程处理查询插入操作
     * 注意 sql居中需要包含$MOD('取模字段',?)=?
     *
     * @param size          线程数
     * @param insertSql     插入语句
     * @param selectSql     查询数据语句
     * @param insertDsName  插入执行数据源名
     * @param selectDsName  查询数据数据源名
     * @param params        查询参数 格式为 key:1 value:"servId"； key值代表查询sql语句中?占位符的位置
     * @param selectHandler 对查询结果进行处理
     * @throws Exception
     */
    public void insertIntoSelect(int size, String insertSql, String selectSql, String insertDsName, String selectDsName, Map<Integer, Object> params, SelectHandler selectHandler) throws Exception {

        if (selectSql.indexOf("$MOD") == -1) {
            throw new Exception("多线程处理查询数据sql语句必须包含$MOD");
        }

        CountDownLatch cdl1 = new CountDownLatch(size);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(size);
        Map<String, ThreadRes> threadMap = new HashMap<String, ThreadRes>();

        for (int i = 0; i < size; i++) {
            executor.execute(new com.ztesoft.mrss.backend.service.common.sql.InsertIntoSelectRunnable(size, i, cdl1, threadMap, insertSql, selectSql, insertDsName, selectDsName, params, dataUtil, selectHandler));
        }
        while (true) {
            if (cdl1.getCount() == 0) {
                log.info("------入表线程执行完了------");
                break;
            }
        }
        executor.shutdownNow();
        JobUtil.getThreadsOne(size, threadMap);
    }

    /**
     * 根据表的分片数开启线程处理查询插入操作
     *
     * @param selectTableName 查询表名
     * @param insertSql       插入语句
     * @param selectSql       查询语句
     * @param insertDsName    插入数据源
     * @param selectDsName    查询数据源
     * @param params          查询参数
     * @param selectHandler   对查询结果进行处理
     * @throws Exception
     */
    public void insertIntoSelect(String selectTableName, String insertSql, String selectSql, String insertDsName, String selectDsName, Map<Integer, Object> params, SelectHandler selectHandler) throws Exception {

        List<String> dataNodes = getDataNodes(selectTableName, selectDsName);
        CountDownLatch cdl1 = new CountDownLatch(dataNodes.size());
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(dataNodes.size());
        Map<String, ThreadRes> threadMap = new HashMap<String, ThreadRes>();

        for (int i = 0; i < dataNodes.size(); i++) {
            executor.execute(new com.ztesoft.mrss.backend.service.common.sql.InsertIntoSelectRunnable(dataNodes.get(i), cdl1, threadMap, insertSql, selectSql, insertDsName, selectDsName, params, dataUtil, selectHandler));
        }
        while (true) {
            if (cdl1.getCount() == 0) {
                log.info("------入表线程执行完了------");
                break;
            }
        }
        executor.shutdownNow();
        JobUtil.getThreadsOne(dataNodes, threadMap);
    }


    public int insertIntoSelect(List<Map<String, Object>> dataList, String insertSql, String insertDsName) {
        List<Object[]> argsList = new ArrayList<>();
        for (Map<String, Object> dto : dataList) {
            List<Object> insertObjList = new ArrayList<>();
            for (String key : dto.keySet()) {
                insertObjList.add(dto.get(key));
            }
            argsList.add(insertObjList.toArray());
        }
        return dataUtil.getDbService(insertDsName).batchUpdate(insertSql, argsList).length;
    }


    public void select(int size, String selectSql, String selectDsName, Map<Integer, Object> params, SelectHandler selectHandler) throws Exception {
        CountDownLatch cdl1 = new CountDownLatch(size);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(size);
        Map<String, ThreadRes> threadMap = new HashMap<String, ThreadRes>();

        for (int i = 0; i < size; i++) {
            executor.execute(new com.ztesoft.mrss.backend.service.common.sql.SelectRunnable(size, i, cdl1, threadMap, selectSql, selectDsName, params, dataUtil, selectHandler));
        }
        while (true) {
            if (cdl1.getCount() == 0) {
                log.info("------入表线程执行完了------");
                break;
            }
        }
        executor.shutdownNow();
        JobUtil.getThreadsOne(size, threadMap);
    }

    public void select(String selectSql, String selectDsName, Map<Integer, Object> params, SelectHandler selectHandler) throws Exception {
        CountDownLatch cdl1 = new CountDownLatch(1);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Map<String, ThreadRes> threadMap = new HashMap<String, ThreadRes>();

        executor.execute(new com.ztesoft.mrss.backend.service.common.sql.SelectRunnable(1, 0, cdl1, threadMap, selectSql, selectDsName, params, dataUtil, selectHandler));

        while (true) {
            if (cdl1.getCount() == 0) {
                log.info("------入表线程执行完了------");
                break;
            }
        }
        executor.shutdownNow();
        JobUtil.getThreadsOne(1, threadMap);
    }

    /**
     * 清空表数据
     *
     * @param tableName 表名
     * @param dsName    表数据源名
     */
    public void truncate(String tableName, String dsName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" truncate table ").append(tableName);
        log.info("truncate:" + sql.toString());
        dataUtil.getDbService(dsName).execute(sql.toString());
    }

    /**
     * 更新sql语句
     *
     * @param sql    sql语句
     * @param dsName 数据源名
     * @return
     */
    public int update(String sql, String dsName) {
        log.info("update:" + sql);
        return dataUtil.getDbService(dsName).update(sql);
    }

    /**
     * 执行sql语句
     *
     * @param sql    sql语句
     * @param dsName 数据源名
     */
    public void execute(String sql, String dsName) {
        log.info("execute:" + sql);
        dataUtil.getDbService(dsName).execute(sql);
    }

    public Map<String, Object> queryMap(String sql, Map<Integer, Object> params, String dsName) {
        List<Object> objList = new ArrayList<>();
        for (int key : params.keySet()) {
            objList.add(params.get(key));
        }
        Map<String, Object> mapList = dataUtil.getDbService(dsName).queryForMap(sql, objList.toArray());
        return mapList;
    }

    public List<Map<String, Object>> queryList(String sql, Map<Integer, Object> params, String dsName) {
        List<Object> objList = new ArrayList<>();
        for (int key : params.keySet()) {
            objList.add(params.get(key));
        }
        List<Map<String, Object>> mapList = dataUtil.getDbService(dsName).queryForList(sql, objList.toArray());
        return mapList;
    }

    /**
     * 批量更新
     *
     * @param sql
     * @param params
     * @param dsName
     * @return
     */
    public int batchUpdate(String sql, List<Map<String, Object>> params, String dsName) {
        List<Object[]> argsList = new ArrayList<>();
        for (Map<String, Object> dto : params) {
            List<Object> insertObjList = new ArrayList<>();
            for (String key : dto.keySet()) {
                insertObjList.add(dto.get(key));
            }
            argsList.add(insertObjList.toArray());
        }
        int num = dataUtil.getDbService(dsName).batchUpdate(sql, argsList).length;
        return num;
    }

    /**
     * 获取表的分片
     *
     * @param tableName 表名
     * @param dsName    数据源名
     * @return
     */
    public List<String> getDataNodes(String tableName, String dsName) {
        List<String> dataNodes = new ArrayList<String>();
        String sql = "/* !HINT({'getDataNodes':'{0}'})*/ SELECT 1";
        sql = sql.replace("{0}", tableName);
        List<Map<String, Object>> dataNodeList = dataUtil.getDbService(dsName).queryForList(sql);
        if (!CollectionUtils.isEmpty(dataNodeList)) {
            for (int i = 0; i < dataNodeList.size(); i++) {
                dataNodes.add(dataNodeList.get(i).get("dataNodes").toString());
            }
        }
        return dataNodes;
    }


}
