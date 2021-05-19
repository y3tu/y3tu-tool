package com.y3tu.tool.lowcode.generator.entity.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 代码生成配置
 *
 * @author y3tu
 */
@Data
@Entity
@Table(name = "gen_config")
public class GenConfig extends BaseEntity {


    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * 数据源配置Id
     */
    @Column(name = "ds_id", columnDefinition = "int(10) comment '数据源ID'")
    private int dsId;

    /**
     * 表名
     */
    @NotBlank
    @Column(name = "table_name", columnDefinition = "varchar(100) comment '表名'")
    private String tableName;

    /**
     * 包路径
     */
    @NotBlank
    @Column(columnDefinition = "varchar(100) comment '包路径'")
    private String pack;

    /**
     * 模块名
     */
    @NotBlank
    @Column(name = "module_name", columnDefinition = "varchar(100) comment '模块名'")
    private String moduleName;

    /**
     * 前端请求
     */
    @Column(name = "api_path", columnDefinition = "varchar(200) comment '前端请求'")
    private String apiPath;

    /**
     * 作者
     */
    @Column(columnDefinition = "varchar(100) comment '作者'")
    private String author;

    /**
     * 表前缀
     */
    @Column(columnDefinition = "varchar(100) comment '表前缀'")
    private String prefix;

}
