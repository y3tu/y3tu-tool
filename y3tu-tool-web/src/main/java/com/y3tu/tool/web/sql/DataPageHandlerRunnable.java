package com.y3tu.tool.web.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程分页处理大批量数据
 *
 * @author y3tu
 */
@Slf4j
public class DataPageHandlerRunnable implements Runnable {

    /**
     * 总线程数
     */
    private final int threadCount;
    /**
     * 当前线程
     */
    private final int currentThread;
    /**
     * 每页数据量 默认5000
     */
    private final int pageSize;
    private final CountDownLatch cdl;
    /**
     * 查询参数
     */
    private final Map<Integer, Object> params;
    private String selectSql;
    private final String selectDsName;
    private final DataHandler dataHandler;
    private final Map<String, ThreadResult> threadResultMap;
    /**
     * 查询结果类型
     */
    private Class clazz;

    public DataPageHandlerRunnable(String selectSql, String selectDsName, Map<Integer, Object> params, DataHandler dataHandler, int threadCount, int currentThread, int pageSize, CountDownLatch cdl, Map<String, ThreadResult> threadResultMap) {
        this.selectSql = selectSql;
        this.selectDsName = selectDsName;
        this.params = params;
        this.dataHandler = dataHandler;
        this.threadCount = threadCount;
        this.currentThread = currentThread;
        this.pageSize = pageSize;
        this.cdl = cdl;
        this.threadResultMap = threadResultMap;
    }

    public DataPageHandlerRunnable(String selectSql, String selectDsName, Class clazz, Map<Integer, Object> params, DataHandler dataHandler, int threadCount, int currentThread, int pageSize, CountDownLatch cdl, Map<String, ThreadResult> threadResultMap) {
        this.selectSql = selectSql;
        this.selectDsName = selectDsName;
        this.clazz = clazz;
        this.params = params;
        this.dataHandler = dataHandler;
        this.threadCount = threadCount;
        this.currentThread = currentThread;
        this.pageSize = pageSize;
        this.cdl = cdl;
        this.threadResultMap = threadResultMap;
    }

    @Override
    public void run() {
        ThreadResult threadResult = new ThreadResult();
        String mapKey = String.format("线程名:%s 线程总数%s 当前线程currentThread:%s", Thread.currentThread().getName(), threadCount, currentThread);
        try {
            call();
            threadResult.setSuccess(true);
        } catch (Exception e) {
            threadResult.setSuccess(false);
            threadResult.setMsg(e.getMessage());
            log.error(e.getMessage(), e);
        } finally {
            threadResultMap.put(mapKey, threadResult);
            if (cdl != null) {
                cdl.countDown();
            }
        }
    }

    /**
     * 线程具体执行逻辑
     */
    void call() {
        //解析查询参数
        List<Object> paramList = new ArrayList<>();
        if (params != null) {
            Object[] key = params.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {
                paramList.add(params.get(key[i]));
            }
        }
        if (selectSql.contains("$MOD")) {
            paramList.add(threadCount);
            paramList.add(currentThread);
            selectSql = selectSql.replace("$MOD", "MOD");
        }
        StringBuilder countSql = new StringBuilder();
        //sql查询数据总量
        countSql.append("select count(*) as count from (").append(selectSql).append(") countTable ");
        Map<String, Object> map = JdbcTemplateContainer.getJdbcTemplate(selectDsName).queryForMap(countSql.toString(), paramList.toArray());
        if (map.get("count") != null) {
            int count = Integer.parseInt(map.get("count").toString());
            log.info("执行SQL语句:" + countSql.toString() + " 总数据量：" + count);
            if (count > 0) {
                int totalPage = 0;
                if (count % pageSize == 0) {
                    totalPage = count / pageSize;
                } else {
                    totalPage = count / pageSize + 1;
                }
                selectSql = selectSql + " limit ?,? ";
                List<Object> paramPageList = new ArrayList<>();
                for (int page = 1; page <= totalPage; page++) {
                    int startPage = (page - 1) * pageSize;
                    paramPageList.addAll(paramList);
                    paramPageList.add(startPage);
                    paramPageList.add(pageSize);
                    log.info("执行SQL语句:" + selectSql);
                    if (clazz != null) {
                        //指定了获取数据的类型
                        List dataList = JdbcTemplateContainer.getJdbcTemplate(selectDsName).query(selectSql, new BeanPropertyRowMapper<>(clazz), paramPageList.toArray());
                        if (dataHandler != null) {
                            dataHandler.handle(dataList);
                        }
                    } else {
                        //默认返回map类型
                        List<Map<String, Object>> dataList = JdbcTemplateContainer.getJdbcTemplate(selectDsName).queryForList(selectSql, paramPageList.toArray());
                        if (dataHandler != null) {
                            dataHandler.handle(dataList);
                        }
                    }
                    paramPageList.clear();
                }
            }
        }
    }

}
