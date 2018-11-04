package com.y3tu.tool.core.convert.impl;

import java.util.TimeZone;

import com.y3tu.tool.core.convert.AbstractConverter;

/**
 * TimeZone转换器
 *
 * @author Looly
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone> {

    @Override
    protected TimeZone convertInternal(Object value) {
        return TimeZone.getTimeZone(convertToStr(value));
    }

}
