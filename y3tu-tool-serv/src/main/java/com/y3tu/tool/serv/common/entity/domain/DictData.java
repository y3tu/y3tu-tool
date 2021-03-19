package com.y3tu.tool.serv.common.entity.domain;

import com.y3tu.tool.web.annotation.Query;
import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报表字典数据
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_dict_data")
@Data
public class DictData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Query(propName = "dictId", type = Query.Type.EQUAL)
    @Column(name = "dict_id", columnDefinition = "int comment '字典ID'")
    Integer dictId;

    @Column(columnDefinition = "varchar(100) comment '字典数据名称'")
    String name;

    @Column(columnDefinition = "varchar(100) comment '字典值'")
    String value;

    @Column(columnDefinition = "varchar(3) comment '状态 00A:有效 00X:失效'")
    String status;

    @Column(columnDefinition = "int comment '排序'")
    Integer seq;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    String remarks;
}
