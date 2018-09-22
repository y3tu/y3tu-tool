package com.y3tu.tool.core.comparator;

import java.util.Comparator;

import com.y3tu.tool.core.collection.ArrayUtil;

/**
 * 按照数组的顺序正序排列，数组的元素位置决定了对象的排序先后<br>
 * 如果参与排序的元素并不在数组中，则排序在前
 *
 * @param <T> 被排序元素类型
 * @author looly
 */
public class IndexedComparator<T> implements Comparator<T> {

    private T[] array;

    /**
     * 构造
     *
     * @param objs 参与排序的数组，数组的元素位置决定了对象的排序先后
     */
    @SuppressWarnings("unchecked")
    public IndexedComparator(T... objs) {
        this.array = objs;
    }

    @Override
    public int compare(T o1, T o2) {
        final int index1 = ArrayUtil.indexOf(array, o1);
        final int index2 = ArrayUtil.indexOf(array, o2);
        return index1 < index2 ? -1 : 1;
    }

}
