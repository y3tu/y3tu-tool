package com.y3tu.tool.core.convert.impl;

import java.util.UUID;

import com.y3tu.tool.core.convert.AbstractConverter;

/**
 * UUID对象转换器转换器
 *
 * @author Looly
 */
public class UUIDConverter extends AbstractConverter<UUID> {

    @Override
    protected UUID convertInternal(Object value) {
        return UUID.fromString(convertToStr(value));
    }

}
