package com.y3tu.tool.report.entity.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 存储配置
 *
 * @author y3tu
 */
@Entity
@Table(name = "storage")
@Data
public class Storage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "int COMMENT '存储类型 1:SFTP;2:FTP,3:七牛;4:阿里oss,5:腾讯oss,6:图床'")
    Integer type;

    @Column(name = "config_json", columnDefinition = "varchar(1000) COMMENT '连接配置Json字符串'")
    String configJson;
}
