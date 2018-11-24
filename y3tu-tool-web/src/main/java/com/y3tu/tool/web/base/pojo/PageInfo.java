package com.y3tu.tool.web.base.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页实体
 *
 * @author y3tu
 * @date 2018/3/1
 */
@Data
public class PageInfo {

    private static final long serialVersionUID = 1L;
    //总记录数
    private long totalCount;
    //每页记录数
    private long pageSize;
    //总页数
    private long totalPage;
    //当前页数
    private long currentPage;
    //列表数据
    private List<?> list;

    /**
     * 分页
     *
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currentPage 当前页数
     */
    public PageInfo(List<?> list, long totalCount, int pageSize, int currentPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage = (long) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public PageInfo(Page<?> page) {
        this.list = page.getRecords();
        this.totalCount = page.getTotal();
        this.pageSize = page.getSize();
        this.currentPage = page.getCurrent();
        this.totalPage = page.getPages();
    }

}
