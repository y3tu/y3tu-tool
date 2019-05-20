package com.y3tu.tool.web.base.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页实体
 *
 * @author y3tu
 * @date 2018/3/1
 */
@Data
public class PageInfo<T> extends Page<T> {

    /**
     * 查询条件参数Map
     */
    Map<String, Object> params = new HashMap<>();


}
