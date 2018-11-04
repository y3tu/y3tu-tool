package com.y3tu.tool.core.lang.mutable;

/**
 * 提供可变值类型接口
 *
 * @param <T> 值得类型
 */
public interface Mutable<T> {

    /**
     * 获得原始值
     *
     * @return 原始值
     */
    T get();

    /**
     * 设置值
     *
     * @param value 值
     */
    void set(T value);

}