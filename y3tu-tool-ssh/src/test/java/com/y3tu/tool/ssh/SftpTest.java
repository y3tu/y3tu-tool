package com.y3tu.tool.ssh;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/26
 */
public class SftpTest {

    @Test
    public void ls() {
        Sftp sftp = new Sftp("www.y3tu.com", 27506, "root", "");
        Console.log(sftp.home());
    }
}