package com.y3tu.tool.report.util;

import net.sf.jasperreports.engine.JasperPrint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperReportUtilTest {

    @Test
    public void handlerHtmlExporter() throws Exception {
        //构造数据
        List<Map> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("test1", "测试");
        JasperPrint jasperPrint = JasperReportUtil.getJasperPrint("D:\\Jasper报表\\test.jasper", list);
        String html = JasperReportUtil.exportToHtml(jasperPrint);
        System.out.println(html);
    }
}