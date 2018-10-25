package com.y3tu.tool.report.http;

import javax.servlet.ServletException;

/**
 * @author y3tu
 * @date 2018/10/25
 */
public class ReportViewServlet extends ResourceServlet {


    public ReportViewServlet() {
        super("report");
    }

    @Override
    protected String process(String url) {
        return null;
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
