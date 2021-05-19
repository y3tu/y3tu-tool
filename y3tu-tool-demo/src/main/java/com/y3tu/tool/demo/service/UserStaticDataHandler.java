package com.y3tu.tool.demo.service;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.staticdata.handler.StaticDataHandler;
import com.y3tu.tool.demo.dto.UserDto;

import java.util.List;

/**
 * @author y3tu
 * @date 2020/12/4
 */
public class UserStaticDataHandler implements StaticDataHandler {
    @Override
    public void handler(String cacheName, Cache cache, Object cacheData) {
        List<UserDto> userDtoList = (List<UserDto>) cacheData;
        for (UserDto userDto : userDtoList) {
            String key = userDto.getName() + userDto.getId();
            cache.get(key, () -> userDto);
        }
    }
}
