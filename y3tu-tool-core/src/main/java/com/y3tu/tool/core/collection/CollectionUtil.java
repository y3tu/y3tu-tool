package com.y3tu.tool.core.collection;

import java.lang.reflect.Type;
import java.util.*;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.y3tu.tool.core.lang.type.Pair;
import com.y3tu.tool.core.reflect.TypeUtil;


/**
 * 集合工具类
 */
public class CollectionUtil {

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || collection.isEmpty();
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && !(collection.isEmpty());
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素，如果collection为空返回null.
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        // 当类型List时，直接取得最后一个元素.
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        return Iterators.getLast(collection.iterator());
    }

    /**
     * 两个集合中的所有元素按顺序相等.
     */
    public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        return Iterables.elementsEqual(iterable1, iterable2);
    }

    ///////////// 求最大最小值，及Top N, Bottom N//////////

    /**
     * 返回无序集合中的最小值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll) {
        return Collections.min(coll);
    }

    /**
     * 返回无序集合中的最小值
     */
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.min(coll, comp);
    }

    /**
     * 返回无序集合中的最大值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
        return Collections.max(coll);
    }

    /**
     * 返回无序集合中的最大值
     */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.max(coll, comp);
    }

    /**
     * 同时返回无序集合中的最小值和最大值，使用元素默认排序
     * <p>
     * 在返回的Pair中，第一个为最小值，第二个为最大值
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll) {
        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (next.compareTo(minCandidate) < 0) {
                minCandidate = next;
            } else if (next.compareTo(maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回无序集合中的最小值和最大值
     * <p>
     * 在返回的Pair中，第一个为最小值，第二个为最大值
     */
    public static <T> Pair<T, T> minAndMax(Collection<? extends T> coll, Comparator<? super T> comp) {

        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (comp.compare(next, minCandidate) < 0) {
                minCandidate = next;
            } else if (comp.compare(next, maxCandidate) > 0) {
                maxCandidate = next;
            }
        }

        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回Iterable中最大的N个对象, back by guava.
     */
    public static <T extends Comparable<?>> List<T> topN(Iterable<T> coll, int n) {
        return Ordering.natural().greatestOf(coll, n);
    }

    /**
     * 返回Iterable中最大的N个对象, back by guava.
     */
    public static <T> List<T> topN(Iterable<T> coll, int n, Comparator<? super T> comp) {
        return Ordering.from(comp).greatestOf(coll, n);
    }

    /**
     * 返回Iterable中最小的N个对象, back by guava.
     */
    public static <T extends Comparable<?>> List<T> bottomN(Iterable<T> coll, int n) {
        return Ordering.natural().leastOf(coll, n);
    }

    /**
     * 返回Iterable中最小的N个对象, back by guava.
     */
    public static <T> List<T> bottomN(Iterable<T> coll, int n, Comparator<? super T> comp) {
        return Ordering.from(comp).leastOf(coll, n);
    }

    /**
     * 将指定对象全部加入到集合中<br>
     * 提供的对象如果为集合类型，会自动转换为目标元素类型<br>
     *
     * @param <T>         元素类型
     * @param collection  被加入的集合
     * @param value       对象，可能为Iterator、Iterable、Enumeration、Array，或者与集合元素类型一致
     * @param elementType 元素类型，为空时，使用Object类型来接纳所有类型
     * @return 被加入集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> addAll(Collection<T> collection, Object value, Type elementType) {
        if (null == collection || null == value) {
            return collection;
        }
        if (null == elementType) {
            // 元素类型为空时，使用Object类型来接纳所有类型
            elementType = Object.class;
        } else {
            final Class<?> elementRowType = TypeUtil.getClass(elementType);
            if (elementRowType.isInstance(value) && false == Iterable.class.isAssignableFrom(elementRowType)) {
                // 其它类型按照单一元素处理
                collection.add((T) value);
                return collection;
            }
        }

        Iterator iter;
        if (value instanceof Iterator) {
            iter = (Iterator) value;
        } else if (value instanceof Iterable) {
            iter = ((Iterable) value).iterator();
        } else if (value instanceof Enumeration) {
            iter = new EnumerationIter<>((Enumeration) value);
        } else if (ArrayUtil.isArray(value)) {
            iter = new ArrayIter<>(value);
        } else {
            throw new CollectionException("Unsupport value type [] !", value.getClass());
        }

        while (iter.hasNext()) {
            try {
                collection.add((T) iter.next());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return collection;
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterable    {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        return IterUtil.join(iterable.iterator(), conjunction, null, null);
    }

}
