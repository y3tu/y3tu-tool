package com.y3tu.tool.core.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.Lists;

/**
 * 关于List的工具集合.
 * <p>
 * 1. 常用函数(如是否为空，sort/binarySearch/shuffle/reverse(via JDK Collection)
 * 2. 各种构造函数(from guava and JDK Collection)
 * 3. 各种扩展List类型的创建函数
 * 4. 集合运算：交集，并集, 差集, 补集，from Commons Collection，但对其不合理的地方做了修正
 *
 * @author y3tu
 */
public class ListUtil {

    /**
     * 判断是否为空
     *
     * @param list list集合
     * @return 是否为空
     */
    public static boolean isEmpty(List<?> list) {
        return (list == null) || list.isEmpty();
    }

    /**
     * 判断是否不为空
     *
     * @param list list集合
     * @return 是否不为空
     */
    public static boolean isNotEmpty(List<?> list) {
        return (list != null) && !(list.isEmpty());
    }

    /**
     * 新建一个ArrayList
     *
     * @return ArrayList对象
     * @see com.google.common.collect.Lists#newArrayList()
     */
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<T>();
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     * @see com.google.common.collect.Lists#newArrayList(Object...)
     */
    public static <T> ArrayList<T> newArrayList(T... values) {
        return Lists.newArrayList(values);
    }

    /**
     * 新建一个ArrayList<br>
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return ArrayList对象
     * @see com.google.common.collect.Lists#newArrayList(Iterable)
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> iterable) {
        return Lists.newArrayList(iterable);
    }

    /**
     * 新建一个ArrayList, 并初始化数组大小.
     *
     * @param initSize 初始化大小
     * @return ArrayList对象
     * @see com.google.common.collect.Lists#newArrayListWithCapacity(int)
     */
    public static <T> ArrayList<T> newArrayListWithCapacity(int initSize) {
        return new ArrayList<T>(initSize);
    }

    /**
     * 新建一个LinkedList.
     *
     * @return LinkedList对象
     * @see com.google.common.collect.Lists#newLinkedList()
     */
    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList<T>();
    }

    /**
     * 新建一个LinkedList.
     *
     * @param iterable {@link Iterable}
     * @return LinkedList对象
     * @see com.google.common.collect.Lists#newLinkedList()
     */
    public static <T> LinkedList<T> newLinkedList(Iterable<? extends T> iterable) {
        return Lists.newLinkedList(iterable);
    }

    /**
     * 新建一个CopyOnWriteArrayList.
     *
     * @return CopyOnWriteArrayList对象
     * @see com.google.common.collect.Lists#newCopyOnWriteArrayList()
     */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<T>();
    }

    /**
     * 新建一个CopyOnWriteArrayList
     *
     * @param iterable {@link Iterable}
     * @return CopyOnWriteArrayList对象
     * @see com.google.common.collect.Lists#newCopyOnWriteArrayList(Iterable)
     */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(T... iterable) {
        return new CopyOnWriteArrayList<T>(iterable);
    }

    /**
     * 数组转为ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(T... values) {
        return newArrayList(values);
    }

    /**
     * 返回一个空的结构特殊的List，节约空间.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     *
     * @return List对象
     * @see Collections#emptyList()
     */
    public static final <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 如果list为null，转化为一个安全的空List.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param list 传入的集合
     * @return List对象
     * @see Collections#emptyList()
     */
    public static <T> List<T> emptyListIfNull(final List<T> list) {
        return list == null ? (List<T>) Collections.EMPTY_LIST : list;
    }

    /**
     * 返回只含一个元素但结构特殊的List，节约空间
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param o 集合包含的对象
     * @return 只含一个元素但结构特殊的List
     * @see Collections#singletonList(Object)
     */
    public static <T> List<T> singletonList(T o) {
        return Collections.singletonList(o);
    }

    /**
     * 返回包装后不可修改的List
     * <p>
     * 如果尝试写入会抛出UnsupportedOperationException.
     *
     * @param list 集合
     * @return 包装后不可修改的List
     * @see Collections#unmodifiableList(List)
     */
    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * 返回包装后同步的List，所有方法都会被synchronized原语同步.
     * <p>
     * 用于CopyOnWriteArrayList与 ArrayDequeue均不符合的场景
     *
     * @param list 集合
     * @return 包装后同步的List
     * @see Collections#synchronizedCollection(Collection)
     */
    public static <T> List<T> synchronizedList(List<T> list) {
        return Collections.synchronizedList(list);
    }

    /**
     * 二分法快速查找对象, 使用Comparable对象自身的比较.
     *
     * @param sortedList list必须已按升序排序.
     * @param key        查询key
     * @return 如果不存在，返回一个负数，代表如果要插入这个对象，应该插入的位置
     * @see Collections#binarySearch(List, Object)
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> sortedList, T key) {
        return Collections.binarySearch(sortedList, key);
    }

    /**
     * 二分法快速查找对象，使用Comparator.
     *
     * @param sortedList list必须已按升序排序.
     * @param key        查询key
     * @param c          排序规则
     * @return 如果不存在，返回一个负数，代表如果要插入这个对象，应该插入的位置
     * @see Collections#binarySearch(List, Object, Comparator)
     */
    public static <T> int binarySearch(List<? extends T> sortedList, T key, Comparator<? super T> c) {
        return Collections.binarySearch(sortedList, key, c);
    }

    /**
     * 随机乱序，使用默认的Random.
     *
     * @param list 集合
     * @see Collections#shuffle(List)
     */
    public static void shuffle(List<?> list) {
        Collections.shuffle(list);
    }

    /**
     * 返回一个倒转顺序访问的List，仅仅是一个倒序的View，不会实际多生成一个List
     *
     * @param list 集合
     * @return 倒转顺序的List
     * @see com.google.common.collect.Lists#reverse(List)
     */
    public static <T> List<T> reverse(final List<T> list) {
        return Lists.reverse(list);
    }

    /**
     * 随机乱序，使用传入的Random.
     *
     * @param list 集合
     * @param rnd  随机规则
     * @see Collections#shuffle(List, Random)
     */
    public static void shuffle(List<?> list, Random rnd) {
        Collections.shuffle(list, rnd);
    }


    /**
     * list1,list2的并集（在list1或list2中的对象），产生新List
     * <p>
     * 对比Apache Common Collection4 ListUtils, 优化了初始大小
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return 结果集
     */
    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> result = new ArrayList<E>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * list1, list2的交集（同时在list1和list2的对象），产生新List
     * <p>
     * copy from Apache Common Collection4 ListUtils，但其做了不合理的去重，因此重新改为性能较低但不去重的版本
     * <p>
     * 与List.retainAll()相比，考虑了的List中相同元素出现的次数, 如"a"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"a".
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return 结果集
     */
    public static <T> List<T> intersection(final List<? extends T> list1, final List<? extends T> list2) {
        List<? extends T> smaller = list1;
        List<? extends T> larger = list2;
        if (list1.size() > list2.size()) {
            smaller = list2;
            larger = list1;
        }

        // 克隆一个可修改的副本
        List<T> newSmaller = new ArrayList<T>(smaller);
        List<T> result = new ArrayList<T>(smaller.size());
        for (final T e : larger) {
            if (newSmaller.contains(e)) {
                result.add(e);
                newSmaller.remove(e);
            }
        }
        return result;
    }

    /**
     * list1, list2的差集（在list1，不在list2中的对象），产生新List.
     * <p>
     * 与List.removeAll()相比，会计算元素出现的次数，如"a"在list1出现两次，而在list2中只出现一次，则差集里会保留一个"a".
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return 结果集
     */
    public static <T> List<T> difference(final List<? extends T> list1, final List<? extends T> list2) {
        final List<T> result = new ArrayList<T>(list1);
        final Iterator<? extends T> iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * list1, list2的补集（在list1或list2中，但不在交集中的对象，又叫反交集）产生新List.
     * <p>
     * copy from Apache Common Collection4 ListUtils，但其并集－交集时，初始大小没有对交集*2，所以做了修改
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return 结果集
     */
    public static <T> List<T> disjoint(final List<? extends T> list1, final List<? extends T> list2) {
        List<T> intersection = intersection(list1, list2);
        List<T> towIntersection = union(intersection, intersection);
        return difference(union(list1, list2), towIntersection);
    }
}
