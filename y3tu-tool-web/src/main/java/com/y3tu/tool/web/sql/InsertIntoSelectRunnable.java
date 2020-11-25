package com.y3tu.tool.web.sql;

import cn.hutool.core.util.StrUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author y3tu
 * @date 2020/10/22
 */
@Slf4j
public class InsertIntoSelectRunnable implements Runnable {

    private int threads;
    private int threadNum;
    private CountDownLatch cdl;
    private Map<String, ThreadRes> threadMap;
    /**
     * 查询参数
     */
    private Map<Integer, Object> params;
    private String insertSql;
    private String selectSql;
    private String insertDsName;
    private String selectDsName;
    private DataUtil dataUtil;
    private SelectHandler selectHandler;

    private String dataNode;

    public InsertIntoSelectRunnable(int threads, int threadNum, CountDownLatch cdl, Map<String, ThreadRes> threadMap, String insertSql, String selectSql, String insertDsName, String selectDsName, Map<Integer, Object> params, DataUtil dataUtil, SelectHandler selectHandler) {
        this.threadNum = threadNum;
        this.threads = threads;
        this.cdl = cdl;
        this.threadMap = threadMap;
        this.insertSql = insertSql;
        this.selectSql = selectSql;
        this.insertDsName = insertDsName;
        this.selectDsName = selectDsName;
        this.params = params;
        this.dataUtil = dataUtil;
        this.selectHandler = selectHandler;
    }

    public InsertIntoSelectRunnable(String dataNode, CountDownLatch cdl, Map<String, ThreadRes> threadMap, String insertSql, String selectSql, String insertDsName, String selectDsName, Map<Integer, Object> params, DataUtil dataUtil, SelectHandler selectHandler) {
        this.dataNode = dataNode;
        this.cdl = cdl;
        this.threadMap = threadMap;
        this.insertSql = insertSql;
        this.selectSql = selectSql;
        this.insertDsName = insertDsName;
        this.selectDsName = selectDsName;
        this.params = params;
        this.dataUtil = dataUtil;
        this.selectHandler = selectHandler;
    }


    @Override
    public void run() {
        ThreadRes threadRes = new ThreadRes();
        String mapKey = threadNum + "";
        try {
            if (StrUtil.isNotBlank(dataNode)) {
                mapKey = dataNode;
                callDataNode();
            } else {
                mapKey = threadNum + "";
                callThread();
            }
            threadRes.setSuccess(true);
        } catch (Exception e) {
            threadRes.setSuccess(false);
            threadRes.setMsg(e.getMessage());
            log.error(e.getMessage(), e);
        } catch (Throwable t) {
            threadRes.setSuccess(false);
            threadRes.setMsg(t.getMessage());
            log.error(t.getMessage());
        }
        cdl.countDown();
        threadMap.put(mapKey, threadRes);
    }

    void callThread() {
        List<Object> paramList = new ArrayList<>();
        if (params != null) {
            Object[] key = params.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {
                paramList.add(params.get(key[i]));
            }
        }
        if (selectSql.indexOf("$MOD") != -1) {
            paramList.add(threads);
            paramList.add(threadNum);
            selectSql = selectSql.replace("$MOD", "MOD");
        }


        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) as count from (").append(selectSql).append(") countTable ");
        Map<String, Object> map = dataUtil.getDbService(selectDsName).queryForMap(countSql.toString(), paramList.toArray());
        if (map.get("count") != null) {
            int count = Integer.valueOf(map.get("count").toString());
            if (count > 0) {
                int pageSize = 200;
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
                    List<Map<String, Object>> mapList = dataUtil.getDbService(selectDsName).queryForList(selectSql, paramPageList.toArray());
                    paramPageList.clear();

                    if (selectHandler != null) {
                        mapList = selectHandler.handle(mapList);
                    }
                    if (!mapList.isEmpty()) {
                        List<Object[]> argsList = new ArrayList<>();
                        for (Map<String, Object> dto : mapList) {
                            List<Object> insertObjList = new ArrayList<>();
                            for (String key : dto.keySet()) {
                                insertObjList.add(dto.get(key));
                            }
                            argsList.add(insertObjList.toArray());
                        }
                        int num = dataUtil.getDbService(insertDsName).batchUpdate(insertSql, argsList).length;
                        log.info("--线程--" + threadNum + "-----入表---:" + num);
                    }

                }
            }
        }
    }


    void callDataNode() {
        List<Object> paramList = new ArrayList<>();
        if (params != null) {
            Object[] key = params.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {
                paramList.add(params.get(key[i]));
            }
        }

        StringBuilder countSql = new StringBuilder();
        countSql.append("/*!HINT({'dn':['{0}']})*/select count(*) as count from (").append(selectSql).append(") countTable ");
        String countSqlStr = countSql.toString();
        countSqlStr = countSqlStr.replace("{0}", dataNode);

        Map<String, Object> map = dataUtil.getDbService(selectDsName).queryForMap(countSqlStr, paramList.toArray());
        if (map.get("count") != null) {
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                int pageSize = 5000;
                int totalPage = 0;
                if (count % pageSize == 0) {
                    totalPage = count / pageSize;
                } else {
                    totalPage = count / pageSize + 1;
                }
                selectSql = "/*!HINT({'dn':['{0}']})*/" + selectSql + "  limit ?,? ";
                selectSql = selectSql.replace("{0}", dataNode);
                List<Object> paramPageList = new ArrayList<>();

                for (int page = 1; page <= totalPage; page++) {
                    int startPage = (page - 1) * pageSize;
                    paramPageList.addAll(paramList);
                    paramPageList.add(startPage);
                    paramPageList.add(pageSize);
                    List<Map<String, Object>> mapList = dataUtil.getDbService(selectDsName).queryForList(selectSql, paramPageList.toArray());

                    paramPageList.clear();

                    if (selectHandler != null) {
                        mapList = selectHandler.handle(mapList);
                    }
                    if (!mapList.isEmpty()) {
                        List<Object[]> argsList = new ArrayList<>();
                        for (Map<String, Object> dto : mapList) {
                            List<Object> insertObjList = new ArrayList<>();
                            for (String key : dto.keySet()) {
                                insertObjList.add(dto.get(key));
                            }
                            argsList.add(insertObjList.toArray());
                        }
                        int num = dataUtil.getDbService(insertDsName).batchUpdate(insertSql, argsList).length;
                        log.info("--线程--" + threadNum + "-----入表---:" + num);
                    }

                }
            }
        }
    }


}
