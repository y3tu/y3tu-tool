package com.y3tu.tool.core.collection;

import java.util.*;

/**
 * 集合相关工具类，包括数组
 * @author y3tu
 */
public class CollectionUtil {

    // ----------------------------------------------------------------------------------------------- new HashSet
    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts 元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return newHashSet(false, ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts 元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(boolean isSorted, T... ts) {
        if (null == ts) {
            return isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
        }
        int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
        HashSet<T> set = isSorted ? new LinkedHashSet<T>(initialCapacity) : new HashSet<T>(initialCapacity);
        for (T t : ts) {
            set.add(t);
        }
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param collection 集合，用于初始化Set
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet<T>(collection) : new HashSet<T>(collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param iter {@link Iterator}
     * @return HashSet对象
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
        if (null == iter) {
            return newHashSet(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param enumration {@link Enumeration}
     * @return HashSet对象
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumration) {
        if (null == enumration) {
            return newHashSet(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
        while (enumration.hasMoreElements()) {
            set.add(enumration.nextElement());
        }
        return set;
    }

    // ----------------------------------------------------------------------------------------------- List
}
