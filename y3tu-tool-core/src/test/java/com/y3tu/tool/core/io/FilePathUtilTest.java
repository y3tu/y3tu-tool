package com.y3tu.tool.core.io;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.text.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletionService;

import static org.junit.Assert.*;

public class FilePathUtilTest {

    @Test
    public void normalizePath() {

        Console.log(FilePathUtil.simplifyPath("/foo//../d"));
    }

    @Test
    public void getAbsolutePathTest() {
        String path = FilePathUtil.getAbsolutePath("D:/中文.xml");
        Console.log(path);
    }

    @Test
    public void normalizeTest() {
        Assert.assertEquals("/foo/", FilePathUtil.normalize("/foo//"));
        Assert.assertEquals("/foo/", FilePathUtil.normalize("/foo/./"));
        Assert.assertEquals("/bar", FilePathUtil.normalize("/foo/../bar"));
        Assert.assertEquals("/bar/", FilePathUtil.normalize("/foo/../bar/"));
        Assert.assertEquals("/baz", FilePathUtil.normalize("/foo/../bar/../baz"));
        Assert.assertEquals("/", FilePathUtil.normalize("/../"));
        Assert.assertEquals("foo", FilePathUtil.normalize("foo/bar/.."));
        Assert.assertEquals("bar", FilePathUtil.normalize("foo/../../bar"));
        Assert.assertEquals("bar", FilePathUtil.normalize("foo/../bar"));
        Assert.assertEquals("/server/bar", FilePathUtil.normalize("//server/foo/../bar"));
        Assert.assertEquals("/bar", FilePathUtil.normalize("//server/../bar"));
        Assert.assertEquals("C:/bar", FilePathUtil.normalize("C:\\foo\\..\\bar"));
        Assert.assertEquals("C:/bar", FilePathUtil.normalize("C:\\..\\bar"));
        Assert.assertEquals("~/bar/", FilePathUtil.normalize("~/foo/../bar/"));
        Assert.assertEquals("bar", FilePathUtil.normalize("~/../bar"));
        Assert.assertEquals("bar", FilePathUtil.normalize("../../bar"));
        Assert.assertEquals("C:/bar", FilePathUtil.normalize("/C:/bar"));
    }

    @Test
    public void doubleNormalizeTest() {
        String normalize = FilePathUtil.normalize("/aa/b:/c");
        String normalize2 = FilePathUtil.normalize(normalize);
        Assert.assertEquals("/aa/b:/c", normalize);
        Assert.assertEquals(normalize, normalize2);
    }
}