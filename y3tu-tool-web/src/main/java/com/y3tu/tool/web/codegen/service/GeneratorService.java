package com.y3tu.tool.web.codegen.service;

import com.y3tu.tool.web.base.pojo.Query;
import com.y3tu.tool.web.codegen.entity.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/7/29
 */
public interface GeneratorService {
    /**
     * 生成代码
     *
     * @param tableNames 表名称
     * @return
     */
    byte[] generatorCode(GenConfig tableNames);

    /**
     * 分页查询表
     *
     * @param query 查询条件
     * @return
     */
    List<Map<String, Object>> queryPage(Query query);
}
