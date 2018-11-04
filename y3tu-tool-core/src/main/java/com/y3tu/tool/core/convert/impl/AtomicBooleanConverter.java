package com.y3tu.tool.core.convert.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import com.y3tu.tool.core.convert.AbstractConverter;
import com.y3tu.tool.core.util.BooleanUtil;

/**
 * {@link AtomicBoolean}转换器
 *
 * @author Looly
 */
public class AtomicBooleanConverter extends AbstractConverter<AtomicBoolean> {

    @Override
    protected AtomicBoolean convertInternal(Object value) {
        if (boolean.class == value.getClass()) {
            return new AtomicBoolean((boolean) value);
        }
        if (value instanceof Boolean) {
            return new AtomicBoolean((Boolean) value);
        }
        final String valueStr = convertToStr(value);
        return new AtomicBoolean(BooleanUtil.toBoolean(valueStr));
    }

}
