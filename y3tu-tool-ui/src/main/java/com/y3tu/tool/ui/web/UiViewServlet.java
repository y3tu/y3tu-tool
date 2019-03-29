package com.y3tu.tool.ui.web;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.http.servlet.AbstractResourceServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author y3tu
 * @date 2019-03-29
 */
@Slf4j
public class UiViewServlet extends AbstractResourceServlet {


    public UiViewServlet(String url) {
        super("y3tu-tool-ui", url);
    }

    /**
     * 对请求的处理
     *
     * @param url
     * @return
     */
    @Override
    protected String process(HttpServletRequest request, String url) {
        try {

            //登录
            if (StrUtil.startWith(url, URLConstant.USER_LOGIN_PAGE)) {
                //todo
            }
            //退出
            if (StrUtil.startWith(url, URLConstant.USER_LOGIN_OUT)) {
                //todo
            }


        } catch (Exception e) {
            log.error("请求异常", e);
            return JsonUtil.toJson(R.error("请求异常:" + e.getMessage()));
        }
        return JsonUtil.toJson(R.warn("服务端没有找到匹配的url:" + url));
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

}
