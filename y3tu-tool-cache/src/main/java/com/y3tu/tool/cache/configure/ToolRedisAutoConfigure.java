package com.y3tu.tool.cache.configure;

import com.alibaba.fastjson.parser.ParserConfig;
import com.y3tu.tool.cache.core.serializer.FastJsonRedisSerializer;
import com.y3tu.tool.cache.core.serializer.StringRedisSerializer;
import com.y3tu.tool.cache.redis.service.RedisService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis自动配置
 *
 * @author y3tu
 */
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureAfter({RedisAutoConfiguration.class})
@Configuration
public class ToolRedisAutoConfigure {

    /**
     * 加载程序默认配置的redis连接
     * 配置自定义redisTemplate
     *
     * @param factory
     * @return
     */
    @Bean("ToolCacheRedisTemplate")
    @Order(1)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // 全局开启AutoType，不建议使用
        // ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // 建议使用这种方式，小范围指定白名单
        ParserConfig.getGlobalInstance().addAccept("com.y3tu.");
        // 设置值（value）的序列化采用KryoRedisSerializer。
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean("ToolCacheRedisService")
    @ConditionalOnBean(name = "ToolCacheRedisTemplate")
    public RedisService redisService(RedisTemplate redisTemplate) {
        return new RedisService(redisTemplate);
    }

}
