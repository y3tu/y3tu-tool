package com.y3tu.tool.core.io.resource;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

public class URLResourceTest {

    @Test
    public void resource() throws IOException {

        File file = URLResourceUtil.asFile(ResourceUtil.asUrl("test/test.txt").toString());
        Console.log(FileUtil.toString(file));
        assertThat(FileUtil.toString(file)).isEqualTo("springside.min=1\nspringside.max=10");

        InputStream is = URLResourceUtil.asStream("classpath:application.properties");
        assertThat(IOUtil.read(is)).isEqualTo("springside.min=1\nspringside.max=10");
        IOUtil.close(is);

        try {
            URLResourceUtil.asFile("classpath:notexist.properties");
            fail("should fail");
        } catch (Throwable t) {
            assertThat(t).isInstanceOf(IllegalArgumentException.class);
        }

        try {
            URLResourceUtil.asStream("classpath:notexist.properties");
            fail("should fail");
        } catch (Throwable t) {
            assertThat(t).isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    public void file() throws IOException {


    }

}
