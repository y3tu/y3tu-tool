package com.y3tu.tool.cache.web.servlet;

import com.y3tu.tool.core.bean.BeanCache;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.http.servlet.AbstractResourceServlet;
import com.y3tu.tool.cache.core.manager.AbstractCacheManager;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.cache.web.service.CacheService;
import com.y3tu.tool.cache.web.constant.URLConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 缓存统计的Servlet
 *
 * @author y3tu
 */
@Slf4j
public class LayerCacheViewServlet extends AbstractResourceServlet {
    public LayerCacheViewServlet() {
        super("http/resources");
    }

    /**
     * 对数据请求的处理
     *
     * @param url
     * @return
     */
    @Override
    protected String process(HttpServletRequest request, HttpServletResponse response, String url) {
        try {

            // 缓存统计列表
            if (StrUtil.startWith(url, URLConstant.CACHE_STATS_LIST)) {
                String cacheName = request.getParameter("cacheName");
                Set<AbstractCacheManager> cacheManagers = AbstractCacheManager.getCacheManager();
                List<CacheStatsInfo> statsList = new ArrayList<>();
                for (AbstractCacheManager cacheManager : cacheManagers) {
                    List<CacheStatsInfo> cacheStats = cacheManager.listCacheStats(cacheName);
                    if (!CollectionUtils.isEmpty(cacheStats)) {
                        statsList.addAll(cacheStats);
                    }
                }
                return JsonUtil.toJson(R.success(statsList));
            }

            // 重置缓存统计数据
            if (StrUtil.startWith(url, URLConstant.RESET_CACHE_STAT)) {
                Set<AbstractCacheManager> cacheManagers = AbstractCacheManager.getCacheManager();
                for (AbstractCacheManager cacheManager : cacheManagers) {
                    cacheManager.resetCacheStat();
                }
                return JsonUtil.toJson(R.success());
            }

            // 删除缓存
            if (StrUtil.startWith(url, URLConstant.CACHE_STATS_DELETE_CACHW)) {
                String cacheNameParam = request.getParameter("cacheName");
                String internalKey = request.getParameter("internalKey");
                String key = request.getParameter("key");
                BeanCache.getBean(CacheService.class).deleteCache(cacheNameParam, internalKey, key);
                return JsonUtil.toJson(R.success());
            }
        } catch (Exception e) {
            log.error("获取缓存统计数据异常", e);
            return JsonUtil.toJson(R.error("获取缓存统计数据异常:" + e.getMessage()));
        }
        return JsonUtil.toJson(R.warn("服务端没有找到匹配的url:" + url));
    }


    @Override
    public void init() throws ServletException {
        super.init();
    }
}
