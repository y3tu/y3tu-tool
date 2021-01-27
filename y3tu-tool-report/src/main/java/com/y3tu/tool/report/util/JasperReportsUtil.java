package com.y3tu.tool.report.util;

import com.lowagie.text.pdf.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * JasperReports报表工具类
 *
 * @author y3tu
 */
@Slf4j
public class JasperReportsUtil {

    /**
     * 报表类型
     */
    public enum REPORT_TYPE {
        /**
         * pdf
         */
        PDF("pdf"),
        /**
         * html
         */
        HTML("html"),
        /**
         * excel
         */
        EXCEL("excel"),
        /**
         * rtf
         */
        RTF("rtf");
        private String value;

        REPORT_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Java类型转换
     */
    public static Map<String, String> TRANS_MAP = new HashMap<String, String>();

    static {
        TRANS_MAP.put("int", "java.lang.Integer");
        TRANS_MAP.put("long", "java.lang.Long");
        TRANS_MAP.put("short", "java.lang.Short");
        TRANS_MAP.put("float", "java.lang.Float");
        TRANS_MAP.put("double", "java.lang.Double");
        TRANS_MAP.put("boolean", "java.lang.Boolean");
        TRANS_MAP.put("char", "java.lang.Character");
        TRANS_MAP.put("byte", "java.lang.Byte");

    }

    /**
     * 报表模板缓存
     */
    public static ConcurrentHashMap<String, JasperReport> REPORT_MAP = new ConcurrentHashMap<>();

    /**
     * 获取报表对象
     *
     * @param templateName 模板名称
     * @param templatePath 报表模板路径
     * @return JasperReport
     */
    public static JasperReport getJasperReport(String templateName, String templatePath) throws JRException {
        if (REPORT_MAP.get(templateName) != null) {
            return REPORT_MAP.get(templateName);
        }
        JasperDesign design = JRXmlLoader.load(templatePath);
        // 编译
        JasperReport report = JasperCompileManager.compileReport(design);
        REPORT_MAP.put(templateName, report);
        return report;
    }

    /**
     * 获取报表对象
     *
     * @param templateName 模板名称
     * @param templateFile 报表文件
     * @return JasperReport
     */
    public static JasperReport getJasperReport(String templateName, File templateFile) throws JRException {
        if (REPORT_MAP.get(templateName) != null) {
            return REPORT_MAP.get(templateName);
        }
        JasperDesign design = JRXmlLoader.load(templateFile);
        // 编译
        JasperReport report = JasperCompileManager.compileReport(design);
        REPORT_MAP.put(templateName, report);
        return report;
    }


    /**
     * 报表输出到前端
     *
     * @param beanList            报表数据
     * @param jasperReport        报表模板
     * @param extraBeanList       报表额外数据
     * @param extraSourceNameList 报表额外数据源名称
     * @param reportType          输出报表格式
     * @param fileName            输出文件名称
     * @param response            响应
     */
    public static String outputReport(List<?> beanList, JasperReport jasperReport, List<List<?>> extraBeanList, List<String> extraSourceNameList, REPORT_TYPE reportType, String fileName, HttpServletResponse response) {
        String reportStr = "";
        // 用beanList填充数据源
        JRDataSource dataSource = new JRBeanCollectionDataSource(beanList);
        try {

            Map<String, Object> reportParameterMap = new HashMap<>();
            // 根据名字设置额外数据源 作为参数
            if (extraSourceNameList != null && extraBeanList != null) {
                for (int i = 0; i < extraSourceNameList.size(); i++) {
                    // 封装beanList为数据源
                    JRDataSource extraDataSource = new JRBeanCollectionDataSource(extraBeanList.get(i));
                    reportParameterMap.put(extraSourceNameList.get(i), extraDataSource);
                }
            }
            // 填充报表
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameterMap, dataSource);

            if (REPORT_TYPE.PDF == reportType) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
                response.setContentType("application/pdf;charset=utf-8");
                //输出pdf
                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } else if (REPORT_TYPE.EXCEL == reportType) {
                //导出Excel
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
                response.setContentType("application/vnd.ms-excel");

                //设置导出时参数
                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setWhitePageBackground(false);
                configuration.setAutoFitPageHeight(true);
                configuration.setDetectCellType(true);
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setConfiguration(configuration);
                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                exporter.setExporterInput(exporterInput);
                //设置输出项
                OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(response.getOutputStream());
                exporter.setExporterOutput(exporterOutput);

                exporter.exportReport();
            } else if (REPORT_TYPE.HTML == reportType) {
                //输出html
                HtmlExporter exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                ByteArrayOutputStream tempOStream = new ByteArrayOutputStream();
                SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(tempOStream);
                exporterOutput.setImageHandler(new HtmlResourceHandler() {
                    Map<String, String> images = new HashMap<>();

                    @Override
                    public void handleResource(String id, byte[] data) {
                        images.put(id, "data:image/jpg;base64," + Base64.encodeBytes(data));
                    }

                    @Override
                    public String getResourcePath(String id) {
                        return images.get(id);
                    }
                });
                exporter.setExporterOutput(exporterOutput);
                exporter.exportReport();
                reportStr = new String(tempOStream.toByteArray(), "UTF-8");
            }
        } catch (Exception e) {
            log.error("生成jasper报表失败", e);
        }
        return reportStr;
    }

    public static String outputReport(JasperReport jasperReport, Connection connection,Map parameters, REPORT_TYPE reportType, String fileName, HttpServletResponse response){
        String reportStr = "";
        try {
            // 填充报表
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            if (REPORT_TYPE.PDF == reportType) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
                response.setContentType("application/pdf;charset=utf-8");
                //输出pdf
                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } else if (REPORT_TYPE.EXCEL == reportType) {
                //导出Excel
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
                response.setContentType("application/vnd.ms-excel");

                //设置导出时参数
                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setWhitePageBackground(false);
                configuration.setAutoFitPageHeight(true);
                configuration.setDetectCellType(true);
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setConfiguration(configuration);
                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                exporter.setExporterInput(exporterInput);
                //设置输出项
                OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(response.getOutputStream());
                exporter.setExporterOutput(exporterOutput);

                exporter.exportReport();
            } else if (REPORT_TYPE.HTML == reportType) {
                //输出html
                HtmlExporter exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                ByteArrayOutputStream tempOStream = new ByteArrayOutputStream();
                SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(tempOStream);
                exporterOutput.setImageHandler(new HtmlResourceHandler() {
                    Map<String, String> images = new HashMap<>();

                    @Override
                    public void handleResource(String id, byte[] data) {
                        images.put(id, "data:image/jpg;base64," + Base64.encodeBytes(data));
                    }

                    @Override
                    public String getResourcePath(String id) {
                        return images.get(id);
                    }
                });
                exporter.setExporterOutput(exporterOutput);
                exporter.exportReport();
                reportStr = new String(tempOStream.toByteArray(), "UTF-8");
            }
        } catch (Exception e) {
            log.error("生成jasper报表失败", e);
        }
        return reportStr;
    }

}
