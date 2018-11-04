package com.y3tu.tool.core.convert.impl;

import com.y3tu.tool.core.convert.AbstractConverter;
import com.y3tu.tool.core.util.BooleanUtil;
import com.y3tu.tool.core.util.StrUtil;

/**
 * 字符转换器
 *
 * @author Looly
 */
public class CharacterConverter extends AbstractConverter<Character> {

    @Override
    protected Character convertInternal(Object value) {
        if (char.class == value.getClass()) {
            return Character.valueOf((char) value);
        } else if (value instanceof Boolean) {
            return BooleanUtil.toCharacter((Boolean) value);
        } else if (boolean.class == value.getClass()) {
            return BooleanUtil.toCharacter((boolean) value);
        } else {
            final String valueStr = convertToStr(value);
            if (StrUtil.isNotBlank(valueStr)) {
                return Character.valueOf(valueStr.charAt(0));
            }
        }
        return null;
    }

}
