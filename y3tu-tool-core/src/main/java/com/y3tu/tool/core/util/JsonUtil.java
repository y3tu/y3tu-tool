package com.y3tu.tool.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON 工具类
 * 使用com.alibaba.fastjson包的方法
 *
 * @author y3tu
 */
public class JsonUtil {


    /**
     * Object转成JSON数据
     *
     * @param object 需要转换的对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.SkipTransientField);
    }

    /**
     * JSON数据，转成Object
     *
     * @param json  JSON字符串
     * @param clazz 目标类型
     * @param <T>   返回类型
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }
}
