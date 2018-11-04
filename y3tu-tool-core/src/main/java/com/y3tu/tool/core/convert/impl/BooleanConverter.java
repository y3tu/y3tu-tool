package com.y3tu.tool.core.convert.impl;

import com.y3tu.tool.core.convert.AbstractConverter;
import com.y3tu.tool.core.util.BooleanUtil;

/**
 * 波尔转换器
 *
 * @author Looly
 */
public class BooleanConverter extends AbstractConverter<Boolean> {

    @Override
    protected Boolean convertInternal(Object value) {
        if (boolean.class == value.getClass()) {
            return Boolean.valueOf((boolean) value);
        }
        String valueStr = convertToStr(value);
        return Boolean.valueOf(BooleanUtil.toBoolean(valueStr));
    }

}
