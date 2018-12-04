package com.y3tu.tool.web.base.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.core.util.ArrayUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页实体
 *
 * @author y3tu
 * @date 2018/3/1
 */
@Data
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String PAGE = "page";
    private static String PAGE_SIZE = "pageSize";
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 每页记录数
     */
    private long pageSize;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 当前页数
     */
    private long currentPage;
    /**
     * 分页列表数据
     */
    private List<T> list;

    /**
     * mybatis-plus page
     */
    private Page<T> page;

    public PageInfo() {
    }

    /**
     * 分页
     *
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currentPage 当前页数
     */
    public PageInfo(List<T> list, long totalCount, int pageSize, int currentPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage = (long) Math.ceil((double) totalCount / pageSize);
    }

    public static <T> PageInfo mapToPageInfo(Map<String, Object> params) {

        PageInfo<T> pageInfo = new PageInfo();
        //分页参数
        pageInfo.currentPage = MapUtil.getInt(params, PAGE);
        pageInfo.pageSize = MapUtil.getInt(params, PAGE_SIZE);

        //mybatis-plus分页
        Page page = new Page<>(pageInfo.currentPage, pageInfo.pageSize);

        //排序
        String[] ascs = MapUtil.get(params, "ascs", String[].class);
        String[] descs = MapUtil.get(params, "descs", String[].class);
        if (ArrayUtil.isNotEmpty(ascs)) {
            page.setAsc(ascs);
        }
        if (ArrayUtil.isNotEmpty(descs)) {
            page.setDesc(descs);
        }

        pageInfo.setPage(page);
        return pageInfo;

    }


}
