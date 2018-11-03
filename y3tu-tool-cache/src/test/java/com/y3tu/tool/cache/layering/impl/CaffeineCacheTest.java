package com.y3tu.tool.cache.layering.impl;

import com.y3tu.tool.cache.layering.impl.CaffeineCache;
import com.y3tu.tool.cache.layering.setting.FirstCacheSetting;
import com.y3tu.tool.cache.layering.support.ExpireMode;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author y3tu
 * @date 2018/11/3
 */
public class CaffeineCacheTest {

    @Test
    public void get() {
        FirstCacheSetting firstCacheSetting1 = new FirstCacheSetting(10, 1000, 4, TimeUnit.SECONDS, ExpireMode.WRITE);
        CaffeineCache caffeineCache = new CaffeineCache("test",firstCacheSetting1,true);
        caffeineCache.put("test","1");
        Console.log(caffeineCache.get("test").toString());
    }
}