package com.y3tu.tool.report.util;

import com.y3tu.tool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JasperReport工具类
 *
 * @author y3tu
 * @date 2019-03-20
 */
@Slf4j
public class JasperUtil {

    /**
     * 报表文件类型
     */
    public static enum REPORT_TYPE {
        //pdf
        PDF,
        //网页html
        HTML,
        //excel
        EXCEL,
        //富文本格式
        RTF
    }

    public static Enum<REPORT_TYPE> getReportType(String type) {
        Enum<REPORT_TYPE> fileType = JasperUtil.REPORT_TYPE.PDF;
        if (type.equals("pdf")) {
            fileType = JasperUtil.REPORT_TYPE.PDF;
        } else if (type.equals("html")) {
            fileType = JasperUtil.REPORT_TYPE.HTML;

        } else if (type.equals("xls")) {
            fileType = JasperUtil.REPORT_TYPE.EXCEL;

        } else if (type.equals("rft")) {
            fileType = JasperUtil.REPORT_TYPE.RTF;
        }
        return fileType;
    }

    static Map<String, String> TRANS_MAP = new HashMap<String, String>();

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
     * 生成报表的html文本
     *
     * @param beanList        数据 需要自己生成
     * @param templatePath    模板路径
     * @param extParameterMap 自定义额外数据源
     * @param webRoot
     * @return
     */
    public static String exportReportHtmlString(List<?> beanList, String templatePath, Map<String, Object> extParameterMap, String webRoot) {
        JRDataSource dataSource = new JRBeanCollectionDataSource(beanList);
        String reportStr = "";
        try {
            String jrxmlPath = webRoot + templatePath;
            JasperDesign design = JRXmlLoader.load(jrxmlPath);

            JasperReport report = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, extParameterMap, dataSource);

            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream tempOStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(tempOStream));
            exporter.exportReport();
            reportStr = new String(tempOStream.toByteArray(), "UTF-8");
        } catch (Exception e) {
            log.error("生成jasper报表失败", e);
        }
        if (extParameterMap.containsKey("imagePath")) {
            reportStr = reportStr.replace("img src=\"\"",
                    "img src=\"" + extParameterMap.get("imagePath") + "\"");
        }
        return reportStr;
    }

    /**
     * 生成报表的html文本
     *
     * @param beanList     数据 需要自己生成
     * @param templatePath 模板路径
     * @param bgImagePath  自定义背景图片地址
     * @param webRoot
     * @return
     */
    public static String exportReportHtmlString(List<?> beanList, String templatePath, String bgImagePath, String webRoot) {
        Map<String, Object> reportParameterMap = new HashMap<String, Object>();
        if (!StrUtil.isEmpty(bgImagePath)) {
            reportParameterMap.put("imagePath", webRoot + bgImagePath);
            reportParameterMap.put("isShowImage", true);
        }
        return exportReportHtmlString(beanList, templatePath, reportParameterMap, webRoot);
    }

    /**
     *
     * @param beanList
     * @param templatePath
     * @param bgImagePath
     * @param webRoot
     * @param extbeanList
     * @param extSourceNameList
     * @return
     */
    public static String exportReportHtmlString(List<?> beanList, String templatePath, String bgImagePath, String webRoot,
                                                List<List<?>> extbeanList, List<String> extSourceNameList) {
        // 用beanList填充数据源
        JRDataSource dataSource = new JRBeanCollectionDataSource(beanList);
        String reportStr = "";
        try {
            // 加载xml
            String jrxmlPath = webRoot + templatePath;
            JasperDesign design = JRXmlLoader.load(jrxmlPath);
            // 编译
            JasperReport report = JasperCompileManager.compileReport(design);
            Map<String, Object> reportParameterMap = new HashMap<String, Object>();
            if (!StrUtil.isEmpty(bgImagePath)) {
                // 设置参数
                reportParameterMap.put("imagePath", webRoot + bgImagePath);
                reportParameterMap.put("isShowImage", true);
            } else {
                reportParameterMap.put("imagePath", "");
                reportParameterMap.put("isShowImage", false);
            }
            // 根据名字设置额外数据源 作为参数
            for (int i = 0; i < extSourceNameList.size(); i++) {
                // 封装beanList为数据源
                JRDataSource extdataSource = new JRBeanCollectionDataSource(extbeanList.get(i));
                reportParameterMap.put(extSourceNameList.get(i), extdataSource);
            }
            // 填充
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, reportParameterMap, dataSource);

            // html格式导出  （正常用JasperExportManager用不同方法导出为各种格式即可 本处是为了直接输出html字符串额外弄的
            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream tempOStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(tempOStream));
            exporter.exportReport();
            reportStr = new String(tempOStream.toByteArray(), "UTF-8");
        } catch (Exception e) {
            log.error("生成jasper报表失败", e);
        }
        reportStr = reportStr.replace("img src=\"\"", "img src=\"" + bgImagePath + "\"");
        return reportStr;
    }

    /*******************************************************************************
     * 方法名称:exportCustomFile  说明：导出自定义文件
     * @param request,HttpServletResponse
     *
     * @ANTHOR GUO.WEI UR(633632) 20150120
     ******************************************************************************/
    public static void exportCustomFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        //纯文件输出
        try {
            String fileName = request.getAttribute("fileName").toString();
//        	new String(request.getAttribute("fileName").toString().getBytes("iso-8859-1"),"utf-8");
            String fileData = request.getAttribute("fileData").toString();
//        	new String(request.getAttribute("fileData").toString().getBytes("iso-8859-1"),"utf-8");
            response.reset();
            response.setContentType("");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(fileData.getBytes("UTF-8"));
            outStream = response.getOutputStream();
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            log.error("生成jasper报表失败", e);
        }
    }


}
