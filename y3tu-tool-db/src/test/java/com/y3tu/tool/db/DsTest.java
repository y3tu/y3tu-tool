package com.y3tu.tool.db;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.y3tu.tool.core.collection.ListUtil;
import com.y3tu.tool.db.ds.DSFactory;
import com.y3tu.tool.db.ds.c3p0.C3p0DSFactory;
import com.y3tu.tool.db.ds.dbcp.DbcpDSFactory;
import com.y3tu.tool.db.ds.druid.DruidDSFactory;
import com.y3tu.tool.db.ds.hikari.HikariDSFactory;
import com.y3tu.tool.db.ds.pooled.PooledDSFactory;
import com.y3tu.tool.db.ds.tomcat.TomcatDSFactory;
import org.junit.Assert;
import org.junit.Test;



/**
 * 数据源单元测试
 * @author Looly
 *
 */
public class DsTest {
	
	@Test
	public void defaultDsTest() throws SQLException{
		DataSource ds = DSFactory.get("mysql");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("sys_user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
	
	@Test
	public void hikariDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new HikariDSFactory());
		DataSource ds = DSFactory.get("mysql");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("sys_user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
	
	@Test
	public void druidDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new DruidDSFactory());
		DataSource ds = DSFactory.get("test");
		
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
	
	@Test
	public void tomcatDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new TomcatDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
	
	@Test
	public void dbcpDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new DbcpDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
	
	@Test
	public void c3p0DsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new C3p0DSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
	
	@Test
	public void hutoolPoolTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new PooledDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(ListUtil.isNotEmpty(all));
	}
}
