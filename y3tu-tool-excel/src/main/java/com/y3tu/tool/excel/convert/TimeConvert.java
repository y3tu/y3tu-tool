package com.y3tu.tool.excel.convert;


import com.y3tu.tool.core.time.DateFormatUtil;
import com.y3tu.tool.core.time.DateUtil;

/**
 * 时间转换
 *
 * @author y3tu
 * @date 2018/4/6
 */
public class TimeConvert implements ExportConvert {
    @Override
    public String handler(Object val) {
        try {
            if (val == null) {
                return "";
            } else {
                return DateFormatUtil.formatStrToStr("yyyy-MM-dd HH:mm:ss",val.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
