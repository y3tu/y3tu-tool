package com.y3tu.tool.web.codegen.entity;

import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.c3p0.C3p0DSFactory;
import cn.hutool.db.ds.dbcp.DbcpDSFactory;
import cn.hutool.db.ds.druid.DruidDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.db.ds.jndi.JndiDSFactory;
import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.db.ds.simple.SimpleDSFactory;
import cn.hutool.db.ds.tomcat.TomcatDSFactory;

/**
 * @author y3tu
 * @date 2018/11/4
 */
public enum  DsFactoryEnum {
    /**
     * 数据库工厂
     */
    C3P0("C3P0", C3p0DSFactory.class),
    DBCP("DBCP", DbcpDSFactory.class),
    Druid("Druid", DruidDSFactory.class),
    Hikari("Hikari", HikariDSFactory.class),
    Jndi("Jndi", JndiDSFactory.class),
    Pooled("Pooled", PooledDSFactory.class),
    Simple("Simple", SimpleDSFactory.class),
    Tomcat("Tomcat", TomcatDSFactory.class);

    private String dsFactoryName;
    private Class<DSFactory> dsFactoryClass;


    DsFactoryEnum(String dsFactoryName, Class dsFactoryClass) {
        this.dsFactoryName = dsFactoryName;
        this.dsFactoryClass = dsFactoryClass;
    }

    public String getDsFactoryName() {
        return dsFactoryName;
    }

    public void setDsFactoryName(String dsFactoryName) {
        this.dsFactoryName = dsFactoryName;
    }

    public Class<DSFactory> getDsFactoryClass() {
        return dsFactoryClass;
    }

    public void setDsFactoryClass(Class dsFactoryClass) {
        this.dsFactoryClass = dsFactoryClass;
    }
}
