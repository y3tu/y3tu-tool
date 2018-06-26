package com.y3tu.tool.core.validator;

import com.y3tu.tool.core.exception.ValidatorException;
import com.y3tu.tool.core.util.StringUtils;

/**
 * @author y3tu
 * @date 2018/6/26
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ValidatorException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ValidatorException(message);
        }
    }
}