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
        Assert.assertEquals(user, "root");

    }

    @Test
    public void load1(){
        File file = FileUtil.file("D:\\work\\test.properties");
        try {
            Console.log(file.getPath());
            Console.log(FileUtil.toLines(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Props props = new Props(file);
        props.autoLoad(true);
        String user = props.getProperty("user");
        Assert.assertEquals(user, "root");

        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Console.log(props.getProperty("user"));
        }
    }

}