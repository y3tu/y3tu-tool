package com.y3tu.tool.core.bean;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/19
 */
public class BeanUtilTest {

    @Test
    public void mapToBean() {

        Map map = new HashMap<>();
        map.put("Name", "yxy");
        map.put("aGe", 28);
        User user = BeanUtil.mapToBean(map, User.class, true);
        Assert.assertEquals(user.getAge(),map.get("aGe"));
    }
}