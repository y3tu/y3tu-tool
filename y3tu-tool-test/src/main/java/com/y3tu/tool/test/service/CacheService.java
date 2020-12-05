package com.y3tu.tool.test.service;

import com.y3tu.tool.cache.annotation.FirstCache;
import com.y3tu.tool.cache.annotation.SecondaryCache;
import com.y3tu.tool.cache.annotation.StaticData;
import com.y3tu.tool.test.dto.UserDto;
import com.y3tu.tool.web.sql.SqlUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务
 *
 * @author y3tu
 */
@Service
public class CacheService {

    @StaticData(value = "user_static",key = "测试key",depict = "用户静态数据",
            firstCache = @FirstCache(expireTime = 2, timeUnit = TimeUnit.MINUTES),
            secondaryCache = @SecondaryCache(expireTime = 4, preloadTime = 1, timeUnit = TimeUnit.MINUTES))
    public List<UserDto> queryUser() {
        String sql = "select id,name,age from user limit 0,5000";
        List<UserDto> list = SqlUtil.queryList(sql, null, UserDto.class, "support");
        return list;
    }


    @StaticData(value = "user_static",key = "测试key1",depict = "用户静态数据",
            firstCache = @FirstCache(expireTime = 2, timeUnit = TimeUnit.MINUTES),
            secondaryCache = @SecondaryCache(expireTime = 4, preloadTime = 1, timeUnit = TimeUnit.MINUTES))
    public List<UserDto> queryUse1() {
        String sql = "select id,name,age from user limit 0,1000";
        List<UserDto> list = SqlUtil.queryList(sql, null, UserDto.class, "support");
        return list;
    }

}
