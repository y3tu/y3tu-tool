package com.y3tu.tool.db.ds;

import com.y3tu.tool.db.ds.c3p0.C3p0DSFactory;
import com.y3tu.tool.db.ds.dbcp.DbcpDSFactory;
import com.y3tu.tool.db.ds.druid.DruidDSFactory;
import com.y3tu.tool.db.ds.hikari.HikariDSFactory;
import com.y3tu.tool.db.ds.jndi.JndiDSFactory;
import com.y3tu.tool.db.ds.pooled.PooledDSFactory;
import com.y3tu.tool.db.ds.simple.SimpleDSFactory;
import com.y3tu.tool.db.ds.tomcat.TomcatDSFactory;

/**
 * 数据源枚举
 *
 * @author y3tu
 * @date 2018/11/5
 */
public enum DsFactoryEnum {

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
