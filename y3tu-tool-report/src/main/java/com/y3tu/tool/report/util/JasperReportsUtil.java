package com.y3tu.tool.report.util;

import com.lowagie.text.pdf.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
     * 生成报表内容
     *
     * @param jasperFilePath 报表模板文件路径
     * @param beanList       数据
     * @return
     * @throws JRException
     */
    public static JasperPrint getJasperPrint(String jasperFilePath, List<?> beanList) throws JRException {
        // 用beanList填充数据源
        JRDataSource dataSource = new JRBeanCollectionDataSource(beanList);
        Map<String, Object> reportParameterMap = new HashMap<>();
        // 填充报表
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, reportParameterMap, dataSource);
        return jasperPrint;
    }

    /**
     * 生成报表内容
     *
     * @param jasperFilePath 报表模板文件路径
     * @param connection   报表内部的数据源连接
     * @param parameters   参数
     * @return
     * @throws JRException
     */
    public static JasperPrint getJasperPrint(String jasperFilePath, Connection connection, Map parameters) throws JRException {
        // 填充报表
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, parameters, connection);
        return jasperPrint;
    }

    public static void handlerExporter(JasperPrint jasperPrint, REPORT_TYPE reportType, String fileName, HttpServletResponse response) throws Exception {
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
    }

    public static String handlerHtmlExporter(JasperPrint jasperPrint) throws Exception {
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

}
