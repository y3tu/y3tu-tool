package com.y3tu.tool.cache.core.servlet;

import com.alibaba.fastjson.JSON;
import com.y3tu.tool.cache.core.manager.AbstractCacheManager;
import com.y3tu.tool.cache.core.service.CacheService;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.BeanCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 统计的Servlet
 *
 * @author yuhao.wang3
 */
@Slf4j
public class LayeringCacheServlet extends HttpServlet {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        // root context
        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());


        // 重置缓存统计数据
        if (UrlConstant.RESET_CACHE_STAT.equals(path)) {
            Set<AbstractCacheManager> cacheManagers = AbstractCacheManager.getCacheManager();
            for (AbstractCacheManager cacheManager : cacheManagers) {
                cacheManager.resetCacheStat();
            }
            response.getWriter().write(JSON.toJSONString(R.success()));
        }

        // 缓存统计列表
        if (UrlConstant.CACHE_STATS_LIST.equals(path)) {
            String cacheName = request.getParameter("cacheName");
            Set<AbstractCacheManager> cacheManagers = AbstractCacheManager.getCacheManager();
            List<CacheStatsInfo> statsList = new ArrayList<>();
            for (AbstractCacheManager cacheManager : cacheManagers) {
                List<CacheStatsInfo> cacheStats = cacheManager.listCacheStats(cacheName);
                if (!CollectionUtils.isEmpty(cacheStats)) {
                    statsList.addAll(cacheStats);
                }
            }
            response.getWriter().write(JSON.toJSONString(R.success(statsList)));
        }

        // 删除缓存
        if (UrlConstant.CACHE_STATS_DELETE_CACHE.equals(path)) {
            String cacheNameParam = request.getParameter("cacheName");
            String internalKey = request.getParameter("internalKey");
            String key = request.getParameter("key");
            BeanCacheUtil.getBean(CacheService.class).deleteCache(cacheNameParam, internalKey, key);
            response.getWriter().write(JSON.toJSONString(R.success()));
        }
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        Set<AbstractCacheManager> cacheManagers = AbstractCacheManager.getCacheManager();
        for (AbstractCacheManager cacheManager : cacheManagers) {
            return cacheManager.getRedisTemplate();
        }
        return null;
    }
}
