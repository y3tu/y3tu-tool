package com.y3tu.tool.core.lang;

/**
 * 过滤器接口
 *
 * @param <T> 被编辑对象类型
 * @author y3tu
 * @date 2018/9/21
 */
@FunctionalInterface
public interface Filter<T> {
    /**
     * 是否接受对象
     *
     * @param t 检查的对象
     * @return 是否接受对象
     */
    boolean accept(T t);
}
