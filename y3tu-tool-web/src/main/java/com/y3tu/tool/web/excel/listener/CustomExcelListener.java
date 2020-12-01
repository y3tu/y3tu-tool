package com.y3tu.tool.web.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用读取excel文件处理
 *
 * @author y3tu
 */
@Slf4j
public abstract class CustomExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 默认每隔100条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private int batchCount = 100;

    List<T> list = new ArrayList<T>();


    public CustomExcelListener() {
    }

    public CustomExcelListener(int batchCount) {
        this.batchCount = batchCount;
    }

    /**
     * 每一条数据解析调用
     *
     * @param data
     * @param analysisContext
     */
    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            handleData(list);
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成后调用
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 读取表头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

    }

    /**
     * 读取表头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {

    }

    /**
     * 数据处理入库操作
     *
     * @param dataList
     */
    protected abstract void handleData(List<T> dataList);


}
