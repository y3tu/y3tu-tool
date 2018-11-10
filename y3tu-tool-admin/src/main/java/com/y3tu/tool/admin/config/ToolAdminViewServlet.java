package com.y3tu.tool.admin.config;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.http.servlet.AbstractResourceServlet;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * 缓存统计的Servlet
 *
 * @author y3tu
 */
@Slf4j
public class ToolAdminViewServlet extends AbstractResourceServlet {
    public ToolAdminViewServlet() {
        super("/html", "layer-cache");
    }

    /**
     * 对数据请求的处理
     *
     * @param url
     * @return
     */
    @Override
    protected String process(HttpServletRequest request, String url) {
        return JsonUtil.toJson(R.ok());
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
