package com.y3tu.tool.db.sql;

/**
 * @author y3tu
 * @date 2018/10/17
 */
public class SqlStr {

    /**
     * 把查询sql根据不同的数据库转换成分页查询语句
     * 目前支持oracle，mysql
     *
     * @param sql        原始查询语句
     * @param pageSize   分页大小
     * @param pageNumber 第几页
     * @param sqlType    数据库类型
     * @return 拼装好的sql语句
     */
    public static String splitSqlByPage(String sql, int pageSize, int pageNumber, SqlType sqlType) {
        StringBuilder strSql = new StringBuilder();
        if (SqlType.ORACLE == sqlType) {
            //oracle
            strSql.append("SELECT * FROM (");
            strSql.append("  SELECT A.*, ROWNUM RN  FROM ( ");
            strSql.append(sql);
            strSql.append(" ) A WHERE ROWNUM<=");
            strSql.append(pageSize * pageNumber);
            strSql.append(" ) ");
            strSql.append(" WHERE RN > ");
            strSql.append((pageNumber - 1) * pageSize);
        } else if (SqlType.MYSQL == sqlType) {
            //mysql
            strSql.append(sql);
            strSql.append(" limit ");
            strSql.append((pageNumber - 1) * pageSize);
            strSql.append(",");
            strSql.append(pageSize);
        }
        return strSql.toString();
    }

    /**
     * 把查询sql封装成查询数据总量的sql
     *
     * @param sql 原始sql语句
     * @return 拼装好的sql
     */
    public static String splitSqlbyCount(String sql) {
        StringBuilder strSql = new StringBuilder();
        strSql.append("select count(1) as count from ( ");
        strSql.append(sql).append(" )");
        return strSql.toString();
    }

    enum SqlType {
        /**
         * oracle
         */
        ORACLE(1),
        /**
         * mysql
         */
        MYSQL(2);

        private final int type;

        SqlType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
