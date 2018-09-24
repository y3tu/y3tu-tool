package com.y3tu.tool.setting;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class PropsTest {

    @Test
    public void load() {
        Props props = new Props("test.properties");
        String user = props.getProperty("user");
        Assert.assertEquals(user, "root1");

    }

    @Test
    public void load1() {
        Props props = new Props("/Users/yxy/work/test.properties");
        props.autoLoad(true);
        String user = props.getProperty("user");
        Assert.assertEquals(user, "root1");
        props.store("/Users/yxy/work/test.properties");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Console.log(props.getProperty("user"));
        }
    }

}