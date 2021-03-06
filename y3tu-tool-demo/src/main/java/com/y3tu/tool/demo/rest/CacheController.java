package com.y3tu.tool.demo.rest;

import com.y3tu.tool.cache.annotation.Cacheable;
import com.y3tu.tool.cache.annotation.FirstCache;
import com.y3tu.tool.cache.annotation.SecondaryCache;
import com.y3tu.tool.cache.service.ToolCacheService;
import com.y3tu.tool.cache.staticdata.StaticDataService;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.demo.dto.UserDto;
import com.y3tu.tool.web.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("cache")
@Slf4j
public class CacheController {

    @Autowired
    StaticDataService staticDataService;

    @Autowired
    ToolCacheService toolCacheService;

    @GetMapping("query/{id}")
    @Cacheable(value = "user", key = "#id", depict = "用户查询缓存",
            firstCache = @FirstCache(expireTime = 1000000, timeUnit = TimeUnit.MILLISECONDS),
            secondaryCache = @SecondaryCache(expireTime = 10000000, preloadTime = 50000, timeUnit = TimeUnit.MILLISECONDS))
    public R query(@PathVariable String id) {
        String sql = "select id,name,age from user where id = " + id;
        List<UserDto> list = SqlUtil.queryList(sql, null, UserDto.class, "support");
        list.stream().forEach(userDto -> log.info(userDto.getName()));
        return R.success(list);
    }

    @GetMapping("queryCache")
    public R queryCache() {
        List<UserDto> list = (List<UserDto>) staticDataService.getStaticData("user_static","测试key");
        return R.success(list.size());
    }

    @GetMapping("queryCache1")
    public R queryCache1() {
        List<UserDto> list = (List<UserDto>) staticDataService.getStaticData("user_static","测试key1");
        return R.success(list.size());
    }

    @GetMapping("clear")
    public void clear(){
        toolCacheService.clearCache("user_static");
    }

}
