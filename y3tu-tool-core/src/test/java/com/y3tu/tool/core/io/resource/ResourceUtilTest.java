package com.y3tu.tool.core.io.resource;

import com.y3tu.tool.core.io.FilePathUtil;
import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.text.ObjectPrintUtil;
import com.y3tu.tool.core.util.RuntimeUtil;
import com.y3tu.tool.core.util.ZipUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarFile;

import static org.assertj.core.api.Assertions.*;

import static org.junit.Assert.*;

public class ResourceUtilTest {
    @Test
    public void test() throws IOException {

        //Console.log(ResourceUtil.toString("test/test.txt"));
       // assertThat(ResourceUtil.toString("test.txt")).contains("ABCDEFG");
        //Console.log(ResourceUtil.toString(ResourceUtilTest.class,"/test/test.txt"));

//        assertThat(ResourceUtil.toString(ResourceUtilTest.class, "/test.txt")).contains("ABCDEFG");
 //       Console.log(ResourceUtil.toLines("test/test.txt"));
//        assertThat(ResourceUtil.toLines("test.txt")).containsExactly("ABCDEFG", "ABC");
//        assertThat(ResourceUtil.toLines(ResourceUtilTest.class, "/test.txt")).containsExactly("ABCDEFG", "ABC");
//
//        // getResoruce 处理重复的资源
 //       Console.log(ResourceUtil.asUrl("META-INF/MANIFEST.MF"));
//        System.out.println(ResourceUtil.asUrl("META-INF/MANIFEST.MF"));
//        assertThat(ResourceUtil.toString("META-INF/MANIFEST.MF")).contains("Manifest");
//
//        // getResources
//        assertThat(ResourceUtil.getResourcesQuietly("META-INF/MANIFEST.MF").size()).isGreaterThan(1);
//
       // ResourceUtil.asUrl("test.txt");
        Console.log(ResourceUtil.toString("LICENSE-junit.txt"));
        Console.log(ResourceUtil.getResourcesQuietly("LICENSE-junit.txt"));
//        System.out.println(ResourceUtil.getResourcesQuietly("META-INF/MANIFEST.MF"));
//
//        assertThat(ResourceUtil.getResourcesQuietly("META-INF/MANIFEST.MF", ResourceUtilTest.class.getClassLoader()).size())
//                .isGreaterThan(1);

    }

    @Test
    public void resourceNameTest() throws IOException {
        Console.log(FilePathUtil.getJarPath(ZipUtil.class));
//        JarFile guavaFile = new JarFile(FilePathUtil.getJarPath(Files.class));
//        assertThat(guavaFile.getEntry("META-INF/MANIFEST.MF")).isNotNull();
//        assertThat(guavaFile.getEntry("/META-INF/MANIFEST.MF")).isNull();
//        guavaFile.close();
    }
}