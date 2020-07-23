package com.y3tu.tool.web.excel.converter;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.y3tu.tool.core.date.DateUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Timestamp 数据类型转换
 */
public class TimestampConverter implements Converter<Timestamp> {

    @Override
    public Class supportJavaTypeKey() {
        return Timestamp.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Timestamp convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        DateTime dateTime = DateUtil.parse(cellData.getStringValue(),DatePattern.NORM_DATETIME_PATTERN);
        return Timestamp.valueOf(dateTime.toString());
    }

    @Override
    public CellData convertToExcelData(Timestamp value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new CellData<>(sdf.format(value));
    }
}
