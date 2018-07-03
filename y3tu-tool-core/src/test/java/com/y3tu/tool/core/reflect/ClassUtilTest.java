package com.y3tu.tool.core.reflect;

import com.y3tu.tool.core.base.RuntimeUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/3
 */
public class ClassUtilTest {

    @Test
    public void getShortClassName() {
        System.out.println(ClassUtil.getShortClassName(RuntimeUtil.class));
    }

    @Test
    public void getAllSuperclasses() {
        System.out.println(ClassUtil.getAllSuperclasses(RuntimeUtil.class));
    }
}