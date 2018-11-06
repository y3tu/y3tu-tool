package com.y3tu.tool.layercache.web.servlet;

import com.y3tu.tool.http.servlet.ResourceServlet;
import javax.servlet.ServletException;


/**
 * 统计的Servlet
 *
 * @author yuhao.wang3
 */
public class LayerCacheViewServlet extends ResourceServlet {
    public LayerCacheViewServlet() {
        super("http/resources", "layer-cache");
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
