package com.y3tu.tool.report.service.impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.report.configure.ToolReportProperties;
import com.y3tu.tool.report.entity.domain.DataSource;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.domain.ReportAttachment;
import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.DataSourceService;
import com.y3tu.tool.report.service.ReportAttachmentService;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.report.util.DataSourceUtil;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.base.jpa.PageInfo;
import com.y3tu.tool.web.excel.ExcelPageData;
import com.y3tu.tool.web.excel.ExcelUtil;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import com.y3tu.tool.web.sql.DataSourceType;
import com.y3tu.tool.web.sql.SqlUtil;
import com.y3tu.tool.web.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.exolab.castor.mapping.xml.Sql;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
public class ReportServiceImpl extends BaseServiceImpl<ReportRepository, Report> implements ReportService {

    @Autowired
    ReportParamService reportParamService;
    @Autowired
    ReportAttachmentService reportAttachmentService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    RemoteFileHelper remoteFileHelper;
    @Autowired
    ToolReportProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReport(ReportDto reportDto) {
        //报表内容
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        report.setCreateTime(new Date());
        report = this.create(report);
        //处理报表参数
        createReportParam(reportDto, report.getId());
        //处理报表模板附件
        if (StrUtil.isNotEmpty(reportDto.getFileName())) {
            //附件上传到远程服务器
            String tempFileName = reportDto.getFileTempPrefix() + ".jrxml";
            String tempFilePath = FileUtil.SYS_TEM_DIR + tempFileName;
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //删除临时目录文件
            FileUtil.del(tempFilePath);
            if (flag) {
                //文件上传成功
                ReportAttachment reportAttachment = new ReportAttachment();
                reportAttachment.setReportId(report.getId());
                reportAttachment.setStatus("00A");
                reportAttachment.setName(reportDto.getFileName());
                reportAttachment.setTempFileName(tempFileName);
                reportAttachment.setRemoteFilePath(properties.getRemotePath() + tempFileName);
                reportAttachment.setCreateTime(new Date());
                reportAttachmentService.create(reportAttachment);
            } else {
                throw new ReportException("文件上传失败！请重新添加报表模板");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReport(ReportDto reportDto) {
        //报表内容
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        report.setUpdateTime(new Date());
        this.update(report);
        //报表参数 先删除旧参数数据再新增
        reportParamService.deleteByReportId(reportDto.getId());
        createReportParam(reportDto, reportDto.getId());
        //处理报表模板附件
        if (StrUtil.isNotEmpty(reportDto.getFileName())) {
            //附件上传到远程服务器
            String tempFileName = reportDto.getFileTempPrefix() + ".jrxml";
            String tempFilePath = FileUtil.SYS_TEM_DIR + tempFileName;
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //删除临时目录文件
            FileUtil.del(tempFilePath);
            if (flag) {
                //文件上传成功 更新记录
                List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(report.getId());
                for (ReportAttachment reportAttachment : reportAttachmentList) {
                    String oldPath = reportAttachment.getRemoteFilePath();
                    reportAttachment.setName(reportDto.getFileName());
                    reportAttachment.setTempFileName(tempFilePath);
                    reportAttachment.setRemoteFilePath(properties.getRemotePath() + tempFilePath);
                    reportAttachment.setUpdateTime(new Date());
                    reportAttachmentService.update(reportAttachment);
                    //删除老附件
                    if (!remoteFileHelper.remove(oldPath)) {
                        throw new ReportException("删除远程服务器上的附件失败！");
                    }
                }
            } else {
                throw new ReportException("文件上传失败！请重新添加报表模板");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(int reportId) {
        //先删除报表参数再删除报表
        reportParamService.deleteByReportId(reportId);
        this.delete(reportId);
        //最后删除附件记录和远程服务器上的附件
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
        reportAttachmentService.deleteByReportId(reportId);
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            if (!remoteFileHelper.remove(reportAttachment.getRemoteFilePath())) {
                throw new ReportException("删除远程服务器上的附件失败！");
            }
        }
    }

    @Override
    public void download(int reportId, HttpServletRequest request, HttpServletResponse response) {
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            remoteFileHelper.download(reportAttachment.getRemoteFilePath(), reportAttachment.getName(), false, request, response);
        }
    }

    @Override
    public R preview(int reportId) {
        Report report = this.getById(reportId);
        if (Report.TYPE_COMMON.equals(report.getType())) {
            //通用报表 todo
        } else if (Report.TYPE_JASPER.equals(report.getType())) {
            Map<String, Object> filePathResult = getJasperTemplate(reportId);
        }
        return null;
    }

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
    public R queryTableData(String sql, int dsId, List<ReportParamDto> params, PageInfo pageInfo) {
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

    private Map<String, Object> getJasperTemplate(int reportId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
            for (ReportAttachment reportAttachment : reportAttachmentList) {
                //先判断临时文件夹下是否已经有模板文件,如果不存在就从远程服务器中获取到报表template
                String jasperFilePath = FileUtil.SYS_TEM_DIR + FileUtil.getFileNameNoEx(reportAttachment.getTempFileName()) + ".jasper";
                String jrxmlFilePath = FileUtil.SYS_TEM_DIR + reportAttachment.getTempFileName();
                if (!FileUtil.exist(jrxmlFilePath)) {
                    remoteFileHelper.download(reportAttachment.getRemoteFilePath(), jrxmlFilePath);
                }
                JasperCompileManager.compileReportToFile(jrxmlFilePath, jasperFilePath);
                result.put("jasperFilePath", jasperFilePath);
                result.put("jrxmlFilePath", jrxmlFilePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


    private void createReportParam(ReportDto reportDto, int reportId) {
        for (int i = 0; i < reportDto.getParams().size(); i++) {
            ReportParam param = new ReportParam();
            BeanUtils.copyProperties(reportDto.getParams().get(i), param);
            param.setReportId(reportId);
            param.setSeq(i);
            param.setCreateTime(new Date());
            reportParamService.create(param);
        }
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
