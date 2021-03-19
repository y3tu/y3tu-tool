package com.y3tu.tool.serv.report.util;

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

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JasperReports报表工具类
 *
 * @author y3tu
 */
@Slf4j
public class JasperReportUtil {

    /**
     * 报表类型
     */
    public enum ReportType {
        HTML,
        PDF,
        XLS,
        XLSX,
        XML,
        RTF,
        CSV,
        DOC
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
     * xlsx分页大小
     */
    public static final int XLSX_PAGE_SIZE = 5000;
    /**
     * xlsx每个sheet页最大数据条数
     */
    public static final int XLSX_SHEET_MAX_ROW = 1000000;
    /**
     * xls分页大小
     */
    public static final int XLS_PAGE_SIZE = 5000;
    /**
     * xls每个sheet页最大数据条数
     */
    public static final int XLS_SHEET_MAX_ROW = 60000;


    private static String getContentType(ReportType type) {
        String contentType;
        switch (type) {
            case HTML:
                contentType = "text/html;charset=utf-8";
                break;
            case PDF:
                contentType = "application/pdf";
                break;
            case XLS:
                contentType = "application/vnd.ms-excel";
                break;
            case XLSX:
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case XML:
                contentType = "text/xml";
                break;
            case RTF:
                contentType = "application/rtf";
                break;
            case CSV:
                contentType = "text/plain";
                break;
            case DOC:
                contentType = "application/msword";
                break;
            default:
                contentType = "";
        }
        return contentType;
    }

    /**
     * 获取报表对象
     *
     * @param templatePath 报表模板路径
     * @return JasperReport
     */
    public static JasperReport getJasperReport(String templatePath) throws JRException {
        JasperDesign design = JRXmlLoader.load(templatePath);
        // 编译
        return JasperCompileManager.compileReport(design);
    }

    /**
     * 生成报表内容
     *
     * @param jasperReport 报表模板
     * @param parameters   参数
     * @param beanList     数据
     * @return
     * @throws JRException
     */
    public static JasperPrint getJasperPrint(JasperReport jasperReport, Map parameters, List<?> beanList) throws JRException {
        // 用beanList填充数据源
        JRDataSource dataSource = null;
        if (null == beanList || beanList.size() == 0) {
            dataSource = new JREmptyDataSource();
        } else {
            dataSource = new JRBeanCollectionDataSource(beanList);
        }
        // 填充报表
        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    /**
     * 生成报表内容
     *
     * @param jasperReport 报表模板
     * @param parameters   参数
     * @param connection   报表内部的数据源连接
     * @return
     * @throws JRException
     */
    public static JasperPrint getJasperPrint(JasperReport jasperReport, Map parameters, Connection connection) throws JRException {
        // 填充报表
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        if (jasperPrint.getPages().isEmpty()) {
            //如何为空，手动定义一个空源数据
            JRDataSource dataSource = new JREmptyDataSource();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        }
        return jasperPrint;
    }

    /**
     * 导出报表到pdf文件
     *
     * @param jasperPrint
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void exportToPdf(JasperPrint jasperPrint, String fileName, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), StandardCharsets.UTF_8));
        response.setContentType(getContentType(ReportType.PDF));
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    /**
     * 导出报表到xml文件
     *
     * @param jasperPrint
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void exportToXml(JasperPrint jasperPrint, String fileName, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), StandardCharsets.UTF_8));
        response.setContentType(getContentType(ReportType.XML));
        JasperExportManager.exportReportToXmlStream(jasperPrint, response.getOutputStream());
    }

    /**
     * 导出报表返回html
     *
     * @param jasperPrint
     * @param response
     * @throws Exception
     */
    public static void exportToHtml(JasperPrint jasperPrint, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=utf-8");
        response.setContentType(getContentType(ReportType.HTML));
        HtmlExporter exporter = new HtmlExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
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
    }

    /**
     * 导出报表返回html字符串
     *
     * @param jasperPrint
     * @return
     * @throws Exception
     */
    public static String exportToHtml(JasperPrint jasperPrint) throws Exception {
        //输出html
        HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
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

        String reportStr = new String(tempOStream.toByteArray(), "UTF-8");
        return reportStr;
    }

    /**
     * 导出单sheet页excel
     *
     * @param jasperPrint
     * @param sheetName
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void exportToExcel(JasperPrint jasperPrint, String sheetName, String fileName, HttpServletResponse response) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        jasperPrintList.add(jasperPrint);
        List<String> sheetNameList = new ArrayList<>();
        sheetNameList.add(sheetName);
        exportToExcel(jasperPrintList, sheetNameList.toArray(new String[0]), fileName, response);
    }

    /**
     * 导出多sheet页excel
     *
     * @param jasperPrintList
     * @param sheetNames
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void exportToExcel(List<JasperPrint> jasperPrintList, String[] sheetNames, String fileName, HttpServletResponse response) throws Exception {
        //导出Excel
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;" + "filename=" + new String(fileName.getBytes(), StandardCharsets.UTF_8));
        response.setContentType("application/vnd.ms-excel");
        //设置导出时参数
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setWhitePageBackground(false);
        configuration.setOnePagePerSheet(false);
        configuration.setIgnorePageMargins(true);
        configuration.setAutoFitPageHeight(true);
        configuration.setDetectCellType(true);
        //设置sheet名字
        configuration.setSheetNames(sheetNames);
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setConfiguration(configuration);
        //设置输入项
        ExporterInput exporterInput = SimpleExporterInput.getInstance(jasperPrintList);
        exporter.setExporterInput(exporterInput);
        //设置输出项
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(response.getOutputStream());
        exporter.setExporterOutput(exporterOutput);
        exporter.exportReport();
    }

}