package com.y3tu.tool.web.base.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页实体
 *
 * @author y3tu
 * @date 2018/3/1
 */
@Data
public class PageInfo {

    public PageInfo() {
        pageNum = 1;
        pageSize = 10;
    }

    public PageInfo(Integer pageSize) {
        this.pageNum = 1;
        this.pageSize = pageSize;
    }

    /**
     * 当前页数
     */
    private int pageNum;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private int count;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 结果集
     */
    List pojoList = new ArrayList();



    public int getPageCount() {

        pageCount = count / pageSize;
        if (count % pageSize > 0) {
            ++pageCount;
        }
        return pageCount;
    }


}
