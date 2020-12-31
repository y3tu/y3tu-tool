package com.y3tu.tool.report.domain;

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
public class Dict {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "varchar(100) comment '字典名称'")
    String name;

    @Column(columnDefinition = "varchar(100) comment '字典编码'")
    String code;

    @Column(columnDefinition = "int comment '字典类型'")
    int type;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    String remarks;

    public enum DictType {
        /**
         * 普通字典
         */
        DATA(0),
        /**
         * SQL字典
         */
        SQL(1);

        private int value;

        DictType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
