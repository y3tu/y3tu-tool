package com.y3tu.tool.web.sql;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
    private int threadCount;
    /**
     * 当前线程
     */
    private int currentThread;
    private CountDownLatch cdl;
    /**
     * 查询参数
     */
    private Map<Integer, Object> params;
    private String selectSql;
    private String selectDsName;
    private DataHandler dataHandler;
    private Map<String, ThreadResult> threadResultMap;

    public DataPageHandlerRunnable(String selectSql, String selectDsName, Map<Integer, Object> params, DataHandler dataHandler, int threadCount, int currentThread, CountDownLatch cdl, Map<String, ThreadResult> threadResultMap) {
        this.selectSql = selectSql;
        this.selectDsName = selectDsName;
        this.params = params;
        this.dataHandler = dataHandler;
        this.threadCount = threadCount;
        this.currentThread = currentThread;
        this.cdl = cdl;
        this.threadResultMap = threadResultMap;
    }

    @Override
    public void run() {
        ThreadResult threadResult = new ThreadResult();
        String mapKey = String.format("线程名:%s 当前线程currentThread:%s", Thread.currentThread().getName(), currentThread);
        try {
            call();
        } catch (Exception e) {
            threadResult.setSuccess(false);
            threadResult.setMsg(e.getMessage());
            log.error(e.getMessage());
        }

        if (cdl != null) {
            cdl.countDown();
        }

        threadResult.setSuccess(true);
        threadResultMap.put(mapKey, threadResult);
    }

    /**
     * 线程具体执行逻辑
     */
    void call() {
        List<Object> objList = new ArrayList<>();
        //解析查询参数
        for (int key : params.keySet()) {
            objList.add(params.get(key));
        }
        if (selectSql.contains("mod") || selectSql.contains("MOD")) {
            objList.add(threadCount);
            objList.add(currentThread);
        }
        StringBuilder countSql = new StringBuilder();
        //sql查询数据总量
        countSql.append("select count(*) as count from (").append(selectSql).append(") countTable ");
        Map<String, Object> map = JdbcTemplateUtil.getJdbcTemplate(selectDsName).queryForMap(countSql.toString(), objList.toArray());
        log.info("执行SQL语句:" + countSql.toString());
        if (map.get("count") != null) {
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                //限制分页数据为5000
                int pageSize = 5000;
                int totalPage = 0;
                if (count % pageSize == 0) {
                    totalPage = count / pageSize;
                } else {
                    totalPage = count / pageSize + 1;
                }
                for (int page = 1; page <= totalPage; page++) {
                    int startPage = (page - 1) * pageSize;

                    selectSql = selectSql + " limit ?,? ";
                    objList.add(startPage);
                    objList.add(pageSize);
                    List<Map<String, Object>> dataList = JdbcTemplateUtil.getJdbcTemplate(selectDsName).queryForList(selectSql, objList.toArray());
                    log.info("执行SQL语句:" + selectSql);
                    if (dataHandler != null) {
                        dataHandler.handle(dataList);
                    }
                }
            }
        }
    }

}
