package com.y3tu.tool.core.collection;

import com.y3tu.tool.core.annotation.Nullable;
import com.y3tu.tool.core.text.StringUtils;

import java.util.Iterator;

/**
 * {@link Iterable} 和 {@link Iterator} 相关工具类
 *
 * @author Looly
 * @since 3.1.0
 */
public class IterUtil {

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterator    集合
     * @param conjunction 分隔符
     * @param prefix      每个元素添加的前缀，null表示不添加
     * @param suffix      每个元素添加的后缀，null表示不添加
     * @return 连接后的字符串
     */
    public static <T> String join(Iterator<T> iterator, CharSequence conjunction, @Nullable String prefix, @Nullable String suffix) {
        if (null == iterator) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        T item;
        while (iterator.hasNext()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }

            item = iterator.next();
            if (ArrayUtil.isArray(item)) {
                sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction, prefix, suffix));
            } else if (item instanceof Iterable<?>) {
                sb.append(join(((Iterable) item).iterator(), conjunction, prefix, suffix));
            } else if (item instanceof Iterator<?>) {
                sb.append(join((Iterator<?>) item, conjunction, prefix, suffix));
            } else {
                sb.append(StringUtils.wrap(String.valueOf(item), prefix, suffix));
            }
        }
        return sb.toString();
    }
}
