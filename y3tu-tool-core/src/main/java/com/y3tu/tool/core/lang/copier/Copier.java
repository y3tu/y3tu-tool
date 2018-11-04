package com.y3tu.tool.core.lang.copier;

/**
 * 拷贝接口
 *
 * @param <T> 拷贝目标类型
 * @author Looly
 */
public interface Copier<T> {
    /**
     * 执行拷贝
     *
     * @return 拷贝的目标
     */
    T copy();
}
