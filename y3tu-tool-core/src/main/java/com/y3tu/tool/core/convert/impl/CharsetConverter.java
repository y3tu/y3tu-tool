package com.y3tu.tool.core.convert.impl;

import com.y3tu.tool.core.text.CharsetUtil;
import org.apache.commons.beanutils.Converter;

/**
 * 转换案例
 * Charset类型转换为String
 *
 * @author y3tu
 * @date 2018/8/1
 */
public class CharsetConverter implements Converter {

    @Override
    public <T> T convert(Class<T> aClass, Object o) {
        return (T) CharsetUtil.charset(o.toString());
    }
}
