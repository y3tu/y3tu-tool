package com.y3tu.tool.core.collection;

import java.util.*;

import com.google.common.collect.Sets;

/**
 * 关于Set的工具集合.
 * <p>
 * 1. 各种Set的创建函数, 包括ConcurrentHashSet
 * 2. 集合运算函数(交集，并集等,from guava)
 *
 * @author y3tu
 */
public class SetUtil {

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return newHashSet(false, ts);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(T... ts) {
        return (LinkedHashSet<T>) newHashSet(true, ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts       元素数组
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
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param collection 集合，用于初始化Set
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet<T>(collection) : new HashSet<T>(collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param iter     {@link Iterator}
     * @return HashSet对象
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
     * @param <T>        集合元素类型
     * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param enumration {@link Enumeration}
     * @return HashSet对象
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

    /**
     * 创建HashSet并设置初始大小，因为HashSet内部是HashMap，会计算LoadFactor后的真实大小.
     *
     * @param expectedSize 初始大小
     * @return HashSet对象
     * @see com.google.common.collect.Sets#newHashSetWithExpectedSize(int)
     */
    public static <T> HashSet<T> newHashSetWithCapacity(int expectedSize) {
        return Sets.newHashSetWithExpectedSize(expectedSize);
    }

    /**
     * 构造类型正确的TreeSet, 通过实现了Comparable的元素自身进行排序.
     *
     * @return TreeSet对象
     * @see com.google.common.collect.Sets#newTreeSet()
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Comparable> TreeSet<T> newTreeSet() {
        return new TreeSet<T>();
    }

    /**
     * 构造类型正确的TreeSet, 并设置comparator.
     *
     * @param comparator 比较器
     * @return TreeSet对象
     * @see com.google.common.collect.Sets#newTreeSet(Comparator)
     */
    public static <T> TreeSet<T> newTreeSet(Comparator<? super T> comparator) {
        return Sets.newTreeSet(comparator);
    }

    /**
     * 将集合转换为排序后的TreeSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param comparator 比较器
     * @return treeSet对象
     */
    public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
        final TreeSet<T> treeSet = new TreeSet<T>(comparator);
        for (T t : collection) {
            treeSet.add(t);
        }
        return treeSet;
    }


    /**
     * 返回一个空的结构特殊的Set，节约空间.
     * <p>
     * 注意返回的Set不可写, 写入会抛出UnsupportedOperationException.
     *
     * @return 空Set对象
     * @see Collections#emptySet()
     */
    public static final <T> Set<T> newEmptySet() {
        return Collections.emptySet();
    }

    /**
     * 如果set为null，转化为一个安全的空Set,否则返回原Set
     * <p>
     * 注意返回的Set不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param set
     * @return Set对象
     * @see Collections#emptySet()
     */
    public static <T> Set<T> emptySetIfNull(final Set<T> set) {
        return set == null ? (Set<T>) Collections.EMPTY_SET : set;
    }

    /**
     * 返回只含一个元素但结构特殊的Set，节约空间.
     * <p>
     * 注意返回的Set不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param <T> the class of the objects in the set
     * @param o   the sole object to be stored in the returned set.
     * @return 只含一个元素但结构特殊的Set
     * @see Collections#singleton(Object)
     */
    public static final <T> Set<T> singletonSet(T o) {
        return Collections.singleton(o);
    }

    /**
     * 返回包装后不可修改的Set.
     * <p>
     * 如果尝试修改，会抛出UnsupportedOperationException
     *
     * @param <T> the class of the objects in the set
     * @param s   the set for which an unmodifiable view is to be returned.
     * @return an unmodifiable view of the specified set.
     * @see Collections#unmodifiableSet(Set)
     */
    public static <T> Set<T> unmodifiableSet(Set<? extends T> s) {
        return Collections.unmodifiableSet(s);
    }

    /**
     * 从Map构造Set的大杀器, 可以用来制造各种Set
     *
     * @param <T> the class of the map keys and of the objects in the
     *            returned set
     * @param map the backing map
     * @return the set backed by the map
     * @see Collections#newSetFromMap(Map)
     */
    public static <T> Set<T> newSetFromMap(Map<T, Boolean> map) {
        return Collections.newSetFromMap(map);
    }


    /**
     * set1, set2的并集（在set1或set2的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     *
     * @param set1 集合1
     * @param set2 集合2
     * @return 结果集合
     */
    public static <E> Set<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.union(set1, set2);
    }

    /**
     * set1, set2的交集（同时在set1和set2的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     *
     * @param set1 集合1
     * @param set2 集合2
     * @return 结果集合
     */
    public static <E> Set<E> intersection(final Set<E> set1, final Set<?> set2) {
        return Sets.intersection(set1, set2);
    }

    /**
     * set1, set2的差集（在set1，不在set2中的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     *
     * @param set1 集合1
     * @param set2 集合2
     * @return 结果集合
     */
    public static <E> Set<E> difference(final Set<E> set1, final Set<?> set2) {
        return Sets.difference(set1, set2);
    }

    /**
     * set1, set2的补集（在set1或set2中，但不在交集中的对象，又叫反交集）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     *
     * @param set1 集合1
     * @param set2 集合2
     * @return 结果集合
     */
    public static <E> Set<E> disjoint(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.symmetricDifference(set1, set2);
    }
}
