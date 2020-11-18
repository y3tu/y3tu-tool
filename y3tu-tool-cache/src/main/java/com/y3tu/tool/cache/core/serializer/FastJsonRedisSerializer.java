package com.y3tu.tool.cache.core.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.y3tu.tool.cache.core.support.NullValue;
import com.y3tu.tool.cache.core.support.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.Charset;

/**
 * @author yuhao.wang
 */
@Slf4j
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;


    /**
     * 指定小范围包的序列化和反序列化，具体原因可以参考：
     * <P>https://www.jianshu.com/p/a92ecc33fd0d</P>
     *
     * @param clazz    clazz
     * @param packages 白名单包名，如:"com.xxx."
     */
    public FastJsonRedisSerializer(Class<T> clazz, String... packages) {
        super();
        this.clazz = clazz;
        try {
            ParserConfig.getGlobalInstance().addAccept("com.y3tu.");
            if (packages != null && packages.length > 0) {
                for (String packageName : packages) {
                    ParserConfig.getGlobalInstance().addAccept(packageName);
                }
            }
        } catch (Throwable e) {
            log.warn("fastjson 版本太低，反序列化有被攻击的风险", e);
        }
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            return JSON.toJSONString(new FastJsonSerializerWrapper(t), SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new SerializationException(String.format("FastJsonRedisSerializer 序列化异常: %s, 【JSON：%s】",
                    e.getMessage(), JSON.toJSONString(t)), e);

        }

    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        }

        String str = new String(bytes, DEFAULT_CHARSET);
        try {
            FastJsonSerializerWrapper wrapper = JSON.parseObject(str, FastJsonSerializerWrapper.class);
            switch (Type.parse(wrapper.getType())) {
                case STRING:
                    return (T) wrapper.getContent();
                case OBJECT:
                case SET:

                    if (wrapper.getContent() instanceof NullValue) {
                        return null;
                    }

                    return (T) wrapper.getContent();

                case LIST:

                    return (T) ((JSONArray) wrapper.getContent()).toJavaList(clazz);

                case NULL:

                    return null;
                default:
                    throw new SerializationException("不支持反序列化的对象类型: " + wrapper.getType());
            }
        } catch (Exception e) {
            throw new SerializationException(String.format("FastJsonRedisSerializer 反序列化异常: %s, 【JSON：%s】",
                    e.getMessage(), str), e);
        }
    }
}