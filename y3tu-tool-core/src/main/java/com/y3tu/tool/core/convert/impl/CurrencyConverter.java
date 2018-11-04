package com.y3tu.tool.core.convert.impl;

import java.util.Currency;

import com.y3tu.tool.core.convert.AbstractConverter;

/**
 * 货币{@link Currency} 转换器
 *
 * @author Looly
 */
public class CurrencyConverter extends AbstractConverter<Currency> {

    @Override
    protected Currency convertInternal(Object value) {
        return Currency.getInstance(value.toString());
    }

}
