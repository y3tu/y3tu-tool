package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.util.JasperReportsUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JasperReports报表工具测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/report/jasper")
@Slf4j
public class JasperReportsController {


    @GetMapping("/previewPDF")
    public R previewPDF(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        try {
            //加载模板
            File templateFile = ResourceUtils.getFile("classpath:report/jasper/testReport.jrxml");
            JasperReport jasperReport = JasperReportsUtil.getJasperReport("测试报表模板", templateFile);

            //构造数据
            List<Map> list = new ArrayList<>();
            Map map = new HashMap();
            map.put("test1", "测试");
            result = JasperReportsUtil.exportReport(list, jasperReport, null, null,
                    JasperReportsUtil.REPORT_TYPE.HTML, "TEST.pdf", request, response);
        } catch (Exception e) {
            log.error("预览报表失败", e);
        }
        return R.success(result);
    }
}
