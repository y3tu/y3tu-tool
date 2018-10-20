package com.y3tu.tool.web.codegen.service.impl;

import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.db.meta.MetaUtil;
import com.y3tu.tool.db.meta.Table;
import com.y3tu.tool.web.codegen.entity.GenConfig;
import com.y3tu.tool.web.codegen.service.GeneratorService;
import com.y3tu.tool.web.codegen.util.GenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.util.List;
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

    @Autowired
    private DataSource dataSource;

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
        Table table = this.queryTableMeta(genConfig.getTableName());
        //表字段信息
        String[] columns = MetaUtil.getColumnNames(dataSource, table.getTableName());
        //生成代码
        GenUtils.generatorCode(genConfig, table, columns, zip);
        IOUtil.close(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询数据源全部表名
     *
     * @return
     */
    @Override
    public List<String> queryTableName() {
        return MetaUtil.getTables(dataSource);
    }

    /**
     * 获取表和表的字段元数据
     *
     * @param tableName
     * @return
     */
    @Override
    public Table queryTableMeta(String tableName) {
        return MetaUtil.getTableMeta(dataSource, tableName);
    }


}
