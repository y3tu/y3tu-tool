package com.y3tu.tool.report.service.impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.report.entity.domain.DataSource;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.service.CommonReportService;
import com.y3tu.tool.report.service.DataSourceService;
import com.y3tu.tool.report.util.DataSourceUtil;
import com.y3tu.tool.web.base.jpa.PageInfo;
import com.y3tu.tool.web.excel.ExcelPageData;
import com.y3tu.tool.web.excel.ExcelUtil;
import com.y3tu.tool.web.sql.DataSourceType;
import com.y3tu.tool.web.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class CommonReportServiceImpl implements CommonReportService {

    @Autowired
    DataSourceService dataSourceService;

    @Override
    public R parseSql(String sql, int dsId) {
        DataSource dataSource = dataSourceService.getById(dsId);
        //获取数据源
        javax.sql.DataSource ds = DataSourceUtil.getDataSource(dataSource);
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Connection connection = ds.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            List<Map<String, String>> fieldNameList = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                String fieldName = metaData.getColumnName(i);
                Map<String, String> fieldNameMap = new HashMap<>();
                fieldNameMap.put("field", fieldName);
                fieldNameMap.put("label", fieldName);
                //默认宽度100
                fieldNameMap.put("width", "100");
                fieldNameList.add(fieldNameMap);
            }
            return R.success(fieldNameList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("解析SQL异常:" + e.getMessage());
        }
    }

    @Override
    public R queryReportData(ReportDto reportDto) {
        int dsId = reportDto.getDsId();
        String sql = reportDto.getQuerySql();
        List<ReportParamDto> params = reportDto.getParams();
        PageInfo pageInfo = reportDto.getPageInfo();
        DataSource dataSource = dataSourceService.getById(dsId);
        javax.sql.DataSource ds = DataSourceUtil.getDataSourceByDsId(dsId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        sql = replaceParamSql(sql, params);
        //首选查询数据总数
        int count = SqlUtil.count(sql, jdbcTemplate);
        pageInfo.setTotal(count);
        if (count > 0) {
            //如果有数据再分页查询
            if (dataSource.getDbType().equals(DataSource.TYPE_MYSQL)) {
                //mysql
                sql = SqlUtil.buildPageSql(DataSourceType.MYSQL, sql, pageInfo.getCurrent() - 1, pageInfo.getSize());
            } else if (dataSource.getDbType().equals(DataSource.TYPE_ORACLE)) {
                //oracle
                sql = SqlUtil.buildPageSql(DataSourceType.ORACLE, sql, pageInfo.getCurrent() - 1, pageInfo.getSize());
            }

            List<Map<String, Object>> list = SqlUtil.queryList(sql, null, jdbcTemplate);

            pageInfo.setRecords(list);
        } else {
            pageInfo.setRecords(null);
        }

        return R.success(pageInfo);
    }

    @Override
    public void export(ReportDto reportDto, HttpServletResponse response) {
        //组装excel表头数据
        String tableHeader = reportDto.getTableHeader();
        String sql = reportDto.getQuerySql();
        int dsId = reportDto.getDsId();
        List<ReportParamDto> params = reportDto.getParams();
        Map<String, Object> headerMap = getHeader(JSONObject.parseArray(tableHeader));
        List<List<String>> headerList = (List<List<String>>) headerMap.get("headerList");
        List<String> fieldList = (List<String>) headerMap.get("fieldList");
        final String querySql = replaceParamSql(sql, params);

        DataSource dataSource = dataSourceService.getById(dsId);
        javax.sql.DataSource ds = DataSourceUtil.getDataSourceByDsId(dsId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        ExcelUtil.downExcelByThreadAndPage(20, reportDto.getName(), reportDto.getName(), null, headerList, ExcelTypeEnum.XLSX, new ExcelPageData<List<Object>>() {
            @Override
            public List<List<Object>> queryDataByPage(int startNum, int pageSize) {
                StringBuilder selectSql = new StringBuilder();
                if (dataSource.getDbType().equals(DataSource.TYPE_MYSQL)) {
                    //mysql
                    selectSql.append(querySql + " limit ?,? ");
                } else if (dataSource.getDbType().equals(DataSource.TYPE_ORACLE)) {
                    //oracle
                    selectSql.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ from (").append(sql)
                            .append(" ) row_ where rownum <=").append(startNum + pageSize).append(") table_alias where table_alias.rownum_ >")
                            .append(startNum);
                }

                Map<String, Object> paramMap = new LinkedHashMap(2);
                paramMap.put("startNum", startNum);
                paramMap.put("pageSize", pageSize);
                List<Map<String, Object>> dataList = SqlUtil.queryList(selectSql.toString(), paramMap, jdbcTemplate);

                List<List<Object>> resultList = new ArrayList<>();
                for (Map<String, Object> data : dataList) {
                    List<Object> result = new ArrayList<>();
                    for (String field : fieldList) {
                        result.add(data.get(field));
                    }
                    resultList.add(result);
                }
                return resultList;
            }
        }, response);
    }

    /**
     * 把参数值替换进sql配置语句中获取到真正执行的sql
     *
     * @param sql
     * @param params
     * @return
     */
    private String replaceParamSql(String sql, List<ReportParamDto> params) {
        //替换sql参数
        for (ReportParamDto param : params) {
            String field = param.getField();
            String $field = "${" + field + "}";
            Object value = param.getValue();
            //最终参数值
            String result = "";
            if (value instanceof List) {
                //如果参数值是数组或者集合需要转换为字符串已逗号分隔
                List<String> valueList = (List<String>) value;
                if (valueList.size() > 0) {
                    result = result + "(";
                    for (String val : valueList) {
                        result = result + val + ",";
                    }
                    result = result.substring(0, result.length() - 1);
                    result = result + ")";
                }
            } else {
                if (value != null) {
                    result = value.toString();
                }
            }

            //处理$ifnull[]，如果参数值为空，删除$ifnull[]包含的内容
            String[] ifnulls = StrUtil.subBetweenAll(sql, "$ifnull[", "]");
            for (String ifnull : ifnulls) {
                if (StrUtil.containsIgnoreCase(ifnull, $field)) {
                    if (StrUtil.isEmpty(result)) {
                        //如果参数值为空,删除$ifnull[]包含的内容
                        sql = sql.replace("$ifnull[" + ifnull + "]", "");
                    } else {
                        sql = sql.replace("$ifnull[" + ifnull + "]", ifnull);
                    }
                }
            }

            sql = sql.replace("${" + field + "}", result);

        }
        return sql;
    }

    /**
     * 装置excel表头
     */
    private Map<String, Object> getHeader(JSONArray jsonArray) {
        Map<String, Object> result = new HashMap<>();
        List<List<String>> headerList = new ArrayList();
        List<String> fieldList = new ArrayList<>();
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            List<String> heads = new ArrayList<>();
            JSONObject object = jsonArray.getJSONObject(i);
            handleHeaderChildren(headerList, heads, fieldList, object);
        }
        result.put("headerList", headerList);
        result.put("fieldList", fieldList);
        return result;
    }

    private void handleHeaderChildren(List<List<String>> headerList, List<String> list, List<String> fieldList, JSONObject object) {
        JSONArray jsonArray = object.getJSONArray("children");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            int size = jsonArray.size();
            list.add(object.getString("label"));
            for (int i = 0; i < size; i++) {
                handleHeaderChildren(headerList, list, fieldList, jsonArray.getJSONObject(i));
            }
        } else {
            fieldList.add(object.getString("field"));
            List<String> tempList = new ArrayList<>();
            tempList.addAll(list);
            tempList.add(object.getString("label"));
            headerList.add(tempList);
        }
    }
}
