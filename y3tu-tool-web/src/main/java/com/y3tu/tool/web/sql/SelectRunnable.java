package com.y3tu.tool.web.sql;

import com.ztesoft.mrss.backend.service.common.DataUtil;
import com.ztesoft.mrss.backend.service.protojava.vo.ThreadRes;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author y3tu
 * @date 2020/10/22
 */
@Slf4j
public class SelectRunnable implements Runnable {

    private int threads;
    private int threadNum;
    private CountDownLatch cdl;
    private Map<String, ThreadRes> threadMap;
    /**
     * 查询参数
     */
    private Map<Integer, Object> params;
    private String selectSql;
    private String selectDsName;
    private DataUtil dataUtil;
    private SelectHandler selectHandler;

    public SelectRunnable(int threads, int threadNum, CountDownLatch cdl, Map<String, ThreadRes> threadMap, String selectSql, String selectDsName, Map<Integer, Object> params, DataUtil dataUtil, SelectHandler selectHandler) {
        this.threadNum = threadNum;
        this.threads = threads;
        this.cdl = cdl;
        this.threadMap = threadMap;
        this.selectSql = selectSql;
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
            call();
        } catch (Exception e) {
            threadRes.setSuccess(false);
            threadRes.setMsg(e.getMessage());
            log.info(e.getMessage());
        } catch (Throwable t) {
            threadRes.setSuccess(false);
            threadRes.setMsg(t.getMessage());
            log.info(t.getMessage());
        }
        cdl.countDown();
        threadMap.put(mapKey, threadRes);
    }

    void call() {
        List<Object> objList = new ArrayList<>();
        for (int key : params.keySet()) {
            objList.add(params.get(key));
        }
        if (selectSql.contains("mod") || selectSql.contains("MOD")) {
            objList.add(threads);
            objList.add(threadNum);
        }
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) as count from (").append(selectSql).append(") countTable ");
        Map<String, Object> map = dataUtil.getDbService(selectDsName).queryForMap(countSql.toString(), objList.toArray());
        if (map.get("count") != null) {
            int count = Integer.valueOf(map.get("count").toString());
            if (count > 0) {
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
                    List<Map<String, Object>> mapList = dataUtil.getDbService(selectDsName).queryForList(selectSql, objList.toArray());
                    if (selectHandler != null) {
                        selectHandler.handle(mapList);
                    }

                }
            }
        }
    }

}
