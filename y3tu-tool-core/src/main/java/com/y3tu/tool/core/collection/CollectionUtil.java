package com.y3tu.tool.core.collection;

import java.lang.reflect.Type;
import java.util.*;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.lang.Editor;
import com.y3tu.tool.core.lang.Filter;
import com.y3tu.tool.core.lang.type.Pair;
import com.y3tu.tool.core.reflect.ClassUtil;
import com.y3tu.tool.core.reflect.ReflectionUtil;
import com.y3tu.tool.core.reflect.TypeUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.core.util.PageUtil;


/**
 * 集合工具类
 *
 * @author y3tu
 */
public class CollectionUtil {

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || collection.isEmpty();
    }

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && !(collection.isEmpty());
    }


    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     *
     * @param collection 集合数据源
     * @param <T>        集合里面的数据类型
     * @return 集合里面的第一个元素
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
     *
     * @param collection 集合数据源
     * @param <T>        集合里面的数据类型
     * @return 集合里面的最后一个元素
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
     * 获取集合中指定下标的元素值，下标可以为负数，例如-1表示最后一个元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param index      下标，支持负数
     * @return 元素值
     */
    public static <T> T get(Collection<T> collection, int index) {
        if (index < 0) {
            index += collection.size();
        }
        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            return list.get(index);
        } else {
            int i = 0;
            for (T t : collection) {
                if (i > index) {
                    break;
                } else if (i == index) {
                    return t;
                }
                i++;
            }
            //检查越界
            if (index >= i) {
                throw new IndexOutOfBoundsException(StringUtils.format("Length is {} but index is {}", i, index));
            }
        }
        return null;
    }

    /**
     * 获取集合中指定多个下标的元素值，下标可以为负数，例如-1表示最后一个元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param indexes    下标，支持负数
     * @return 元素值列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getAny(Collection<T> collection, int... indexes) {
        final int size = collection.size();
        final ArrayList<T> result = new ArrayList<>();
        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            for (int index : indexes) {
                if (index < 0) {
                    index += size;
                }
                result.add(list.get(index));
            }
        } else {
            Object[] array = ((Collection<T>) collection).toArray();
            for (int index : indexes) {
                if (index < 0) {
                    index += size;
                }
                result.add((T) array[index]);
            }
        }
        return result;
    }

    /**
     * 判断指定集合是否包含指定值，如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     *
     * @param collection 集合
     * @param value      需要查找的值
     * @return 如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     */
    public static boolean contains(final Collection<?> collection, Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，既是两个集合是否至少有一个共同的元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     */
    public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据集合返回一个元素计数的 {@link Map}<br>
     * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
     * 例如：[a,b,c,c,c] 得到：<br>
     * a: 1<br>
     * b: 1<br>
     * c: 3<br>
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return {@link Map}
     * @see IterUtil#countMap(Iterable)
     */
    public static <T> Map<T, Integer> countMap(Iterable<T> collection) {
        return IterUtil.countMap(collection);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterable    {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterable, CharSequence)
     */
    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        return IterUtil.join(iterable, conjunction);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterator    集合
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterator, CharSequence)
     */
    public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
        return IterUtil.join(iterator, conjunction);
    }

    /**
     * 创建新的集合对象
     *
     * @param <T>            集合类型
     * @param collectionType 集合类型
     * @return 集合类型对应的实例
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> create(Class<?> collectionType) {
        Collection<T> list = null;
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            // 抽象集合默认使用ArrayList
            list = new ArrayList<>();
        }

        // Set
        else if (collectionType.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet<>();
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet<>();
        } else if (collectionType.isAssignableFrom(EnumSet.class)) {
            list = (Collection<T>) EnumSet.noneOf((Class<Enum>) ClassUtil.getTypeArgument(collectionType));
        }

        // List
        else if (collectionType.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            list = new LinkedList<>();
        }

        // Others，直接实例化
        else {
            try {
                list = (Collection<T>) ReflectionUtil.newInstance(collectionType);
            } catch (Exception e) {
                throw new UtilException(e);
            }
        }
        return list;
    }

    /**
     * 去重集合
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return {@link ArrayList}
     */
    public static <T> ArrayList<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        } else if (collection instanceof Set) {
            return new ArrayList<>(collection);
        } else {
            return new ArrayList<>(new LinkedHashSet<>(collection));
        }
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @param step  步进
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     */
    public static <T> List<T> sub(List<T> list, int start, int end, int step) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final int size = list.size();
        if (start < 0) {
            start += size;
        }
        if (end < 0) {
            end += size;
        }
        if (start == size) {
            return new ArrayList<>(0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > size) {
            if (start >= size) {
                return new ArrayList<>(0);
            }
            end = size;
        }

        if (step <= 1) {
            return list.subList(start, end);
        }

        final List<T> result = new ArrayList<>();
        for (int i = start; i < end; i += step) {
            result.add(list.get(i));
        }
        return result;
    }

    /**
     * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param size       每个段的长度
     * @return 分段列表
     */
    public static <T> List<List<T>> split(Collection<T> collection, int size) {
        final List<List<T>> result = new ArrayList<>();

        ArrayList<T> subList = new ArrayList<>(size);
        for (T t : collection) {
            if (subList.size() >= size) {
                result.add(subList);
                subList = new ArrayList<>(size);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }

    /**
     * 过滤<br>
     * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
     *
     * <pre>
     * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
     * 2、修改元素对象，返回集合中为修改后的对象
     * </pre>
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param editor     编辑器接口
     * @return 过滤后的数组
     */
    public static <T> Collection<T> editor(Collection<T> collection, Editor<T> editor) {
        Collection<T> collection2 = ObjectUtil.clone(collection);
        try {
            collection2.clear();
        } catch (UnsupportedOperationException e) {
            // 克隆后的对象不支持清空，说明为不可变集合对象，使用默认的ArrayList保存结果
            collection2 = new ArrayList<>();
        }

        T modified;
        for (T t : collection) {
            modified = editor.edit(t);
            if (null != modified) {
                collection2.add(modified);
            }
        }
        return collection2;
    }

    /**
     * 过滤<br>
     * 过滤过程通过传入的Filter实现来过滤返回需要的元素内容，这个Filter实现可以实现以下功能：
     *
     * <pre>
     * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回true的对象将被加入结果集合中
     * </pre>
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param filter     过滤器
     * @return 过滤后的数组
     */
    public static <T> Collection<T> filter(Collection<T> collection, Filter<T> filter) {
        Collection<T> collection2 = ObjectUtil.clone(collection);
        try {
            collection2.clear();
        } catch (UnsupportedOperationException e) {
            // 克隆后的对象不支持清空，说明为不可变集合对象，使用默认的ArrayList保存结果
            collection2 = new ArrayList<>();
        }

        for (T t : collection) {
            if (filter.accept(t)) {
                collection2.add(t);
            }
        }
        return collection2;
    }

    /**
     * 去除{@code null} 元素
     *
     * @param collection 集合
     * @return 处理后的集合
     */
    public static <T> Collection<T> removeNull(Collection<T> collection) {
        return editor(collection, new Editor<T>() {
            @Override
            public T edit(T t) {
                // 返回null便不加入集合
                return t;
            }
        });
    }

    /**
     * 去掉集合中的多个元素
     *
     * @param collection  集合
     * @param elesRemoved 被去掉的元素数组
     * @return 原集合
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> removeAny(Collection<T> collection, T... elesRemoved) {
        collection.removeAll(SetUtil.newHashSet(elesRemoved));
        return collection;
    }

    /**
     * 去除{@code null}或者"" 元素
     *
     * @param collection 集合
     * @return 处理后的集合
     */
    public static <T extends CharSequence> Collection<T> removeEmpty(Collection<T> collection) {
        return filter(collection, new Filter<T>() {
            @Override
            public boolean accept(T t) {
                return false == StringUtils.isEmpty(t);
            }
        });
    }

    /**
     * 去除{@code null}或者""或者空白字符串 元素
     *
     * @param collection 集合
     * @return 处理后的集合
     */
    public static <T extends CharSequence> Collection<T> removeBlank(Collection<T> collection) {
        return filter(collection, new Filter<T>() {
            @Override
            public boolean accept(T t) {
                return false == StringUtils.isBlank(t);
            }
        });
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
     * 将另一个列表中的元素加入到列表中，如果列表中已经存在此元素则忽略之
     *
     * @param <T>       集合元素类型
     * @param list      列表
     * @param otherList 其它列表
     * @return 此列表
     */
    public static <T> List<T> addAllIfNotContains(List<T> list, List<T> otherList) {
        for (T t : otherList) {
            if (false == list.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 反序给定List，会在原List基础上直接修改
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 反序给定List，会创建一个新的List，原List数据不变
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     */
    public static <T> List<T> reverseNew(List<T> list) {
        final List<T> list2 = ObjectUtil.clone(list);
        return reverse(list2);
    }

    /**
     * 设置或增加元素。当index小于List的长度时，替换指定位置的值，否则在尾部追加
     *
     * @param list    List列表
     * @param index   位置
     * @param element 新元素
     * @return 原List
     */
    public static <T> List<T> setOrAppend(List<T> list, int index, T element) {
        if (index < list.size()) {
            list.set(index, element);
        } else {
            list.add(element);
        }
        return list;
    }

    //-----------------------------------------sort

    /**
     * 排序集合，排序不会修改原集合
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param comparator 比较器
     * @return treeSet
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        List<T> list = new ArrayList<T>(collection);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * 针对List排序，排序会修改原List
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     * @return 原list
     * @see Collections#sort(List, Comparator)
     */
    public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, c);
        return list;
    }

    /**
     * 将多个集合排序并显示不同的段落（分页）<br>
     * 采用先排序，后截断的方式取分页的部分
     *
     * @param <T>        集合元素类型
     * @param pageNo     页码，从1开始
     * @param numPerPage 每页的条目数
     * @param comparator 比较器
     * @param colls      集合数组
     * @return 分页后的段落内容
     */
    @SafeVarargs
    public static <T> List<T> sortPageAll(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
        final List<T> result = new ArrayList<>();
        for (Collection<T> coll : colls) {
            result.addAll(coll);
        }

        Collections.sort(result, comparator);

        int resultSize = result.size();
        // 每页条目数大于总数直接返回所有
        if (resultSize <= numPerPage) {
            return result;
        }
        final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
        if (startEnd[1] > resultSize) {
            // 越界直接返回空
            return new ArrayList<>();
        }

        return result.subList(startEnd[0], startEnd[1]);
    }

    /**
     * 将多个集合排序并显示不同的段落（分页）<br>
     * 采用{@link BoundedPriorityQueue}实现分页取局部
     *
     * @param <T>        集合元素类型
     * @param pageNo     页码
     * @param numPerPage 每页的条目数
     * @param comparator 比较器
     * @param colls      集合数组
     * @return 分业后的段落内容
     */
    @SafeVarargs
    public static <T> List<T> sortPageAll2(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
        BoundedPriorityQueue<T> queue = new BoundedPriorityQueue<>(pageNo * numPerPage, comparator);
        for (Collection<T> coll : colls) {
            queue.addAll(coll);
        }

        int resultSize = queue.size();
        // 每页条目数大于总数直接返回所有
        if (resultSize <= numPerPage) {
            return queue.toList();
        }
        final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
        if (startEnd[1] > resultSize) {
            // 越界直接返回空
            return new ArrayList<>();
        }

        return queue.toList().subList(startEnd[0], startEnd[1]);
    }

    /**
     * 两个集合中的所有元素按顺序相等.
     *
     * @param iterable1 集合1
     * @param iterable2 集合2
     * @return 两个集合是否有相同的顺序
     */
    public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        return Iterables.elementsEqual(iterable1, iterable2);
    }


    /**
     * 返回无序集合中的最小值，使用元素默认排序
     *
     * @param collection 集合
     * @return 无序集合中的最小值
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> collection) {
        return Collections.min(collection);
    }

    /**
     * 返回集合中的最小值
     *
     * @param collection 集合
     * @param comparator 排序器
     * @return 集合中的最小值
     */
    public static <T> T min(Collection<? extends T> collection, Comparator<? super T> comparator) {
        return Collections.min(collection, comparator);
    }

    /**
     * 返回无序集合中的最大值，使用元素默认排序
     *
     * @param collection 集合
     * @return 集合中的最大值
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> collection) {
        return Collections.max(collection);
    }

    /**
     * 返回集合中的最大值
     *
     * @param collection 集合
     * @param comparator 排序器
     * @return 集合中的最大值
     */
    public static <T> T max(Collection<? extends T> collection, Comparator<? super T> comparator) {
        return Collections.max(collection, comparator);
    }

    /**
     * 同时返回无序集合中的最小值和最大值，使用元素默认排序
     * <p>
     * 在返回的Pair中，第一个为最小值，第二个为最大值
     *
     * @param collection 集合
     * @return 返回无序集合中的最小值和最大值
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> collection) {
        Iterator<? extends T> i = collection.iterator();
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
     * 返回集合中的最小值和最大值
     * <p>
     * 在返回的Pair中，第一个为最小值，第二个为最大值
     *
     * @param collection 集合
     * @param comparator 排序器
     * @return 返回无序集合中的最小值和最大值
     */
    public static <T> Pair<T, T> minAndMax(Collection<? extends T> collection, Comparator<? super T> comparator) {

        Iterator<? extends T> i = collection.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (comparator.compare(next, minCandidate) < 0) {
                minCandidate = next;
            } else if (comparator.compare(next, maxCandidate) > 0) {
                maxCandidate = next;
            }
        }

        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回Iterable中最大的N个对象
     *
     * @param iterable 集合
     * @param n        多少个对象
     * @return 返回Iterable中最大的N个对象
     */
    public static <T extends Comparable<?>> List<T> topN(Iterable<T> iterable, int n) {
        return Ordering.natural().greatestOf(iterable, n);
    }

    /**
     * 返回Iterable中最大的N个对象
     *
     * @param iterable   集合
     * @param n          多少个对象
     * @param comparator 排序器
     * @return 返回Iterable中最大的N个对象
     */
    public static <T> List<T> topN(Iterable<T> iterable, int n, Comparator<? super T> comparator) {
        return Ordering.from(comparator).greatestOf(iterable, n);
    }

    /**
     * 返回Iterable中最小的N个对象
     *
     * @param iterable 集合
     * @param n        多少个对象
     * @return 返回Iterable中最大的N个对象
     */
    public static <T extends Comparable<?>> List<T> bottomN(Iterable<T> iterable, int n) {
        return Ordering.natural().leastOf(iterable, n);
    }

    /**
     * 返回Iterable中最小的N个对象
     *
     * @param iterable   集合
     * @param n          多少个对象
     * @param comparator 排序器
     * @return 返回Iterable中最小的N个对象
     */
    public static <T> List<T> bottomN(Iterable<T> iterable, int n, Comparator<? super T> comparator) {
        return Ordering.from(comparator).leastOf(iterable, n);
    }

}
