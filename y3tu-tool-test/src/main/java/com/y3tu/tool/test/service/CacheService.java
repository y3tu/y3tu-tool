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

    @StaticData(value = "user_static", depict = "用户静态数据",
            handler = UserStaticDataHandler.class,
            firstCache = @FirstCache(expireTime = 1000000, timeUnit = TimeUnit.MILLISECONDS),
            secondaryCache = @SecondaryCache(expireTime = 10000000, preloadTime = 50000, timeUnit = TimeUnit.MILLISECONDS))
    public List<UserDto> queryUser() {
        String sql = "select id,name,age from user limit 0,100";
        List<UserDto> list = SqlUtil.queryList(sql, null, UserDto.class, "support");
        return list;
    }
}
