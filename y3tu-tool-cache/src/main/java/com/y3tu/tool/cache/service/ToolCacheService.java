package com.y3tu.tool.cache.service;

import com.y3tu.tool.cache.core.manager.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 操作缓存的服务
 *
 * @author y3tu
 */
@Service
public class ToolCacheService {

    @Autowired
    CacheManager cacheManager;

}
