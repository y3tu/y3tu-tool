package com.y3tu.tool.setting;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettingTest {
    @Test
    public void settingTest() {
        Setting setting = new Setting("test.setting", true);

        String test = setting.getStr("test");
        Assert.assertEquals("test", test);

        String driver = setting.getByGroup("driver", "demo");
        Assert.assertEquals("com.mysql.jdbc.Driver", driver);

        //本分组变量替换
        String user = setting.getByGroup("user", "demo");
        Assert.assertEquals("rootcom.mysql.jdbc.Driver", user);

        //跨分组变量替换
        String user2 = setting.getByGroup("user2", "demo");
        Assert.assertEquals("rootcom.mysql.jdbc.Driver", user2);
    }
}