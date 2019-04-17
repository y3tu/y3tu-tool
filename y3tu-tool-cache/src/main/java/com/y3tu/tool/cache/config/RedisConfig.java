package com.y3tu.tool.cache.config;

import com.y3tu.tool.cache.aspect.LimitAspect;
import com.y3tu.tool.cache.aspect.RedisAspect;
import com.y3tu.tool.cache.core.serializer.FastJsonRedisSerializer;
import com.y3tu.tool.cache.redis.lock.RedisDistributedLock;
import com.y3tu.tool.cache.redis.template.RedisRepository;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

/**
 * redis相关注入
 *
 * @author y3tu
 */
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
public class RedisConfig {

    /**
     * 对redis操作的封装
     *
     * @param redisTemplate spring redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedisRepository.class)
    @ConditionalOnBean(RedisTemplate.class)
    public RedisRepository redisRepository(RedisTemplate redisTemplate) {
        return new RedisRepository(redisTemplate);
    }

    /**
     * redis切面
     * @return
     */
    @Bean
    @ConditionalOnClass(Aspect.class)
    public RedisAspect redisAspect(){
        return new RedisAspect();
    }

    /**
     * 限流切面
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public LimitAspect limitAspect(RedisTemplate redisTemplate){
        return new LimitAspect(redisTemplate);
    }


    /**
     * 分布式锁的实现
     *
     * @param redisTemplate spring redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedisDistributedLock.class)
    @ConditionalOnBean(RedisTemplate.class)
    public RedisDistributedLock redisDistributedLock(RedisTemplate redisTemplate) {
        return new RedisDistributedLock(redisTemplate);
    }

    /**
     * JedisPoolConfig 连接池
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "y3tu.tool.cache.redis.enable")
    public JedisPoolConfig jedisPoolConfig(RedisProperties properties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(properties.getMaxIdle());
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(properties.getMaxTotal());
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(properties.getMaxWaitMillis());
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(properties.getNumTestsPerEvictionRun());
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(properties.isTestOnBorrow());
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(properties.isTestWhileIdle());
        return jedisPoolConfig;
    }

    /**
     * 单机版配置
     */
    @Bean
    @ConditionalOnProperty(name = "y3tu.tool.cache.redis.enable")
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig, RedisProperties properties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(properties.getHostName());
        redisStandaloneConfiguration.setPort(properties.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jpb.poolConfig(jedisPoolConfig);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jpb.build());
        return jedisConnectionFactory;
    }

    @Bean(name = "redisTemplate")
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ConditionalOnMissingBean(name = "redisTemplate")
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        //使用 fastjson 序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        // value 值的序列化采用 fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key 的序列化采用 StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(name = "limitRedisTemplate")
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate<String, Serializable> limitRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}

