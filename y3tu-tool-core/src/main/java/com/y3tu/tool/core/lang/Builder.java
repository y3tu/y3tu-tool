package com.y3tu.tool.core.lang;

/**
 * 建造者模式接口定义
 *
 * @param <T> 建造对象类型
 * @author Looly
 */
public interface Builder<T> {
    /**
     * 构建
     *
     * @return 被构建的对象
     */
    T build();
}