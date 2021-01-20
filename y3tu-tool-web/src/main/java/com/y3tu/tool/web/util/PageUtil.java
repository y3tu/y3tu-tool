package com.y3tu.tool.web.util;

import com.y3tu.tool.core.db.SqlTypeEnum;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 *
 * @author y3tu
 * @see cn.hutool.core.util.PageUtil
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     */
    public static List toPage(int page, int size, List list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if (fromIndex > list.size()) {
            return new ArrayList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * Page 数据处理，预防redis反序列化报错
     */
    public static Map<String, Object> toPage(Page page) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getContent());
        map.put("totalElements", page.getTotalElements());
        return map;
    }

    /**
     * 自定义分页
     */
    public static Map<String, Object> toPage(Object object, Object totalElements) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", object);
        map.put("totalElements", totalElements);
        return map;
    }

    /**
     * 拼接sql分页语句
     *
     * @param type
     * @param sql
     * @return
     */
    public static String buildPageSql(SqlTypeEnum type, String sql, int current, int pageSize) {
        StringBuilder pageSql = new StringBuilder();
        int startIndex = current * pageSize;
        int endIndex = (current + 1) * pageSize;
        if (type == SqlTypeEnum.MYSQL) {
            //mysql

            pageSql.append(sql).append("limit ").append(startIndex).append(",").append(endIndex);
        } else if (type == SqlTypeEnum.ORACLE) {
            //oracle
            pageSql.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ from (").append(sql)
                    .append(" ) row_ where rownum <=").append(endIndex).append(") table_alias where table_alias.rownum_ >")
                    .append(startIndex);
        }
        return pageSql.toString();
    }

}
