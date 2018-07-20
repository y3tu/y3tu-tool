package com.y3tu.tool.excel.convert;


import com.y3tu.tool.core.util.DateUtil;

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
                return DateUtil.formatCSTTime(val.toString(), "yyyy-MM-dd HH:mm:ss");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
