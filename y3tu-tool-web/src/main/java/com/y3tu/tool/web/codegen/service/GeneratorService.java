package com.y3tu.tool.web.codegen.service;

import com.y3tu.tool.db.meta.Table;
import com.y3tu.tool.web.codegen.entity.GenConfig;

import java.util.List;

/**
 * @author y3tu
 * @date 2018/10/20
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
     * 查询数据源全部被表名
     *
     * @return
     */
    List<String> queryTableName();

    /**
     * 获取表和表的字段元数据
     *
     * @param tableName
     * @return
     */
    Table queryTableMeta(String tableName);
}
