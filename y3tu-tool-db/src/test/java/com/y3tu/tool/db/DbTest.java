package com.y3tu.tool.db;

import java.sql.SQLException;
import java.util.List;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.db.sql.Condition;
import com.y3tu.tool.db.transaction.TxFunc;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Db对象单元测试
 *
 * @author looly
 */
public class DbTest {

    @Test
    public void findTest() throws SQLException {
        List<Entity> find = Db.use("mysql").find(Entity.create("sys_user").set("phone", "17034642111"));
        Assert.assertEquals("admin", find.get(0).get("username"));
    }

    @Test
    public void findByTest() throws SQLException {
        Db db = Db.use("mysql");

        List<Entity> find = db.findBy("sys_user",
                Condition.parse("dept_id", "> 9"),
                Condition.parse("dept_id", "< 11")
        );
        for (Entity entity : find) {
            Console.log(entity);
        }
        Assert.assertEquals("admin", find.get(0).get("username"));
    }

    @Test
    @Ignore
    public void txTest() throws SQLException {
        Db.use("mysql").tx(new TxFunc() {

            @Override
            public void call(Db db) throws SQLException {
                db.insert(Entity.create("sys_user").set("username", "unitTestUser2")
                        .set("password","ddd")
                        .set("phone","1333333")
                );
                db.update(Entity.create().set("dept_id", 79), Entity.create("sys_user").set("username", "unitTestUser2"));
                db.del("sys_user", "username", "unitTestUser2");
            }
        });
    }
}
