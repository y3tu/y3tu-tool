package com.y3tu.tool.report.http;

import com.y3tu.tool.http.servlet.AbstractResourceServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author y3tu
 * @date 2018/10/25
 */
public class ReportViewServlet extends AbstractResourceServlet {


    public ReportViewServlet() {
        super("report","y3tu-report");
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected String process(HttpServletRequest request, String url) {
        return null;
    }
}
