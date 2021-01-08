package com.y3tu.tool.report.entity.domain;

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
 * 报表字典
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_dict")
@Data
public class Dict extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Query(propName = "name", type = Query.Type.INNER_LIKE)
    @Column(columnDefinition = "varchar(100) comment '字典名称'")
    String name;

    @Query(propName = "code", type = Query.Type.INNER_LIKE)
    @Column(columnDefinition = "varchar(100) comment '字典编码'")
    String code;

    @Query(propName = "type", type = Query.Type.EQUAL)
    @Column(columnDefinition = "varchar(10) comment '字典类型 common:普通字典 sql:SQL字典'")
    String type;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    String remarks;

    public enum DictType {
        /**
         * 普通字典
         */
        COMMON("common"),
        /**
         * SQL字典
         */
        SQL("sql");

        private String value;

        DictType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
