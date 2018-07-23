package com.y3tu.tool.core.collection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.y3tu.tool.core.annotation.Nullable;

/**
 * 数组工具类.
 * 
 * 1. 创建Array的函数
 * 
 * 2. 数组的乱序与contact相加
 * 
 * 3. 从Array转换到Guava的底层为原子类型的List
 * 
 * JDK Arrays的其他函数，如sort(), toString() 请直接调用
 * 
 * Common Lang ArrayUtils的其他函数，如subarray(),reverse(),indexOf(), 请直接调用
 */
public class ArrayUtil {

	/**
	 * 数组是否为空
	 *
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @return 是否为空
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isEmpty(final T... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为非空
	 *
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @return 是否为非空
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isNotEmpty(final T... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 是否包含{@code null}元素
	 *
	 * @param <T> 数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含{@code null}元素
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNull(T... array) {
		if (isNotEmpty(array)) {
			for (T element : array) {
				if (null == element) {
					return true;
				}
			}
		}
		return false;
	}



	/**
	 * 新建一个空数组
	 *
	 * @param <T> 数组元素类型
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return 空数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	/**
	 * 新建一个空数组
	 *
	 * @param <T> 数组元素类型
	 * @param newSize 大小
	 * @return 空数组
	 * @since 3.3.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(int newSize) {
		return (T[]) new Object[newSize];
	}

	/**
	 * 从collection转为Array, 以 list.toArray(new String[0]); 最快 不需要创建list.size()的数组.
	 * 
	 * 本函数等价于list.toArray(new String[0]); 用户也可以直接用后者.
	 * 
	 * https://shipilev.net/blog/2016/arrays-wisdom-ancients/
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> col, Class<T> type) {
		return col.toArray((T[]) Array.newInstance(type, 0));
	}

	/**
	 * Swaps the two specified elements in the specified array.
	 */
	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 将传入的数组乱序
	 */
	public static <T> T[] shuffle(T[] array) {
		if (array != null && array.length > 1) {
			Random rand = new Random();
			return shuffle(array, rand);
		} else {
			return array;
		}
	}

	/**
	 * 将传入的数组乱序
	 */
	public static <T> T[] shuffle(T[] array, Random random) {
		if (array != null && array.length > 1 && random != null) {
			for (int i = array.length; i > 1; i--) {
				swap(array, i - 1, random.nextInt(i));
			}
		}
		return array;
	}

	/**
	 * 添加元素到数组头.
	 */
	public static <T> T[] concat(@Nullable T element, T[] array) {
		return ObjectArrays.concat(element, array);
	}

	/**
	 * 添加元素到数组末尾.
	 */
	public static <T> T[] concat(T[] array, @Nullable T element) {
		return ObjectArrays.concat(array, element);
	}

	////////////////// guava Array 转换为底层为原子类型的List ///////////
	/**
	 * 原版将数组转换为List.
	 * 
	 * 注意转换后的List不能写入, 否则抛出UnsupportedOperationException
	 * 
	 * @see Arrays#asList(Object...)
	 */
	public static <T> List<T> asList(T... a) {
		return Arrays.asList(a);
	}

	/**
	 * Arrays.asList()的加强版, 返回一个底层为原始类型int的List
	 * 
	 * 与保存Integer相比节约空间，同时只在读取数据时AutoBoxing.
	 * 
	 * @see Arrays#asList(Object...)
	 * @see com.google.common.primitives.Ints#asList(int...)
	 * 
	 */
	public static List<Integer> intAsList(int... backingArray) {
		return Ints.asList(backingArray);
	}

	/**
	 * Arrays.asList()的加强版, 返回一个底层为原始类型long的List
	 * 
	 * 与保存Long相比节约空间，同时只在读取数据时AutoBoxing.
	 * 
	 * @see Arrays#asList(Object...)
	 * @see com.google.common.primitives.Longs#asList(long...)
	 */
	public static List<Long> longAsList(long... backingArray) {
		return Longs.asList(backingArray);
	}

	/**
	 * Arrays.asList()的加强版, 返回一个底层为原始类型double的List
	 * 
	 * 与保存Double相比节约空间，同时也避免了AutoBoxing.
	 * 
	 * @see Arrays#asList(Object...)
	 * @see com.google.common.primitives.Doubles#asList(double...)
	 */
	public static List<Double> doubleAsList(double... backingArray) {
		return Doubles.asList(backingArray);
	}


	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 *
	 * @param <T> 数组元素类型
	 * @param buffer 已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> T[] append(T[] buffer, T... newElements) {
		if(isEmpty(buffer)) {
			return newElements;
		}
		return insert(buffer, buffer.length, newElements);
	}

	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 *
	 * @param <T> 数组元素类型
	 * @param array 已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> Object append(Object array, T... newElements) {
		if(isEmpty(array)) {
			return newElements;
		}
		return insert(array, length(array), newElements);
	}


	/**
	 * 获取数组长度<br>
	 * 如果参数为{@code null}，返回0
	 *
	 * <pre>
	 * ArrayUtil.length(null)            = 0
	 * ArrayUtil.length([])              = 0
	 * ArrayUtil.length([null])          = 1
	 * ArrayUtil.length([true, false])   = 2
	 * ArrayUtil.length([1, 2, 3])       = 3
	 * ArrayUtil.length(["a", "b", "c"]) = 3
	 * </pre>
	 *
	 * @param array 数组对象
	 * @return 数组长度
	 * @throws IllegalArgumentException 如果参数不为数组，抛出此异常
	 * @since 3.0.8
	 * @see Array#getLength(Object)
	 */
	public static int length(Object array) throws IllegalArgumentException {
		if (null == array) {
			return 0;
		}
		return Array.getLength(array);
	}

	/**
	 * 将新元素插入到到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
	 *
	 * @param <T> 数组元素类型
	 * @param buffer 已有数组
	 * @param index 插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 * @since 4.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] buffer, int index, T... newElements) {
		return (T[]) insert((Object)buffer, index, newElements);
	}

	/**
	 * 将新元素插入到到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
	 *
	 * @param <T> 数组元素类型
	 * @param array 已有数组
	 * @param index 插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 * @since 4.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object insert(Object array, int index, T... newElements) {
		if (isEmpty(newElements)) {
			return array;
		}
		if(isEmpty(array)) {
			return newElements;
		}

		final int len = length(array);
		if (index < 0) {
			index = (index % len) + len;
		}

		final T[] result = newArray(array.getClass().getComponentType(), Math.max(len, index) + newElements.length);
		System.arraycopy(array, 0, result, 0, Math.min(len, index));
		System.arraycopy(newElements, 0, result, index, newElements.length);
		if (index < len) {
			System.arraycopy(array, index, result, index + newElements.length, len - index);
		}
		return result;
	}


}
