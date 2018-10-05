package com.y3tu.tool.web.codegen.service.impl;

import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.web.base.pojo.Query;
import com.y3tu.tool.web.codegen.entity.GenConfig;
import com.y3tu.tool.web.codegen.mapper.GeneratorMapper;
import com.y3tu.tool.web.codegen.service.GeneratorService;
import com.y3tu.tool.web.codegen.util.GenUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2018-07-30
 */
@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
    private final GeneratorMapper generatorMapper;


    /**
     * 分页查询表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPage(Query query) {
        return generatorMapper.queryList(query);
    }

    /**
     * 生成代码
     *
     * @param genConfig 生成配置
     * @return
     */
    @Override
    public byte[] generatorCode(GenConfig genConfig) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        //查询表信息
        Map<String, String> table = queryTable(genConfig.getTableName());
        //查询列信息
        List<Map<String, String>> columns = queryColumns(genConfig.getTableName());
        //生成代码
        GenUtils.generatorCode(genConfig, table, columns, zip);
        IOUtil.close(zip);
        return outputStream.toByteArray();
    }

    private Map<String, String> queryTable(String tableName) {
        return generatorMapper.queryTable(tableName);
    }

    private List<Map<String, String>> queryColumns(String tableName) {
        return generatorMapper.queryColumns(tableName);
    }
}
