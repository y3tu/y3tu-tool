package com.y3tu.tool.web.base.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Map;


/**
 * 分页实体
 *
 * @author y3tu
 */
@Data
public class PageInfo<T> extends Page<T> {

    /**
     * 查询条件参数实体
     */
    T entity;

    /**
     * 查询条件参数
     */
    Map params;

}
