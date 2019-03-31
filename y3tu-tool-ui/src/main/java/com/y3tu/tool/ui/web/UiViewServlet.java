package com.y3tu.tool.ui.web;

import com.y3tu.tool.core.bean.BeanCache;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.http.ip.IPRange;
import com.y3tu.tool.http.servlet.AbstractResourceServlet;
import com.y3tu.tool.ui.service.AuthService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author y3tu
 * @date 2019-03-29
 */
@Slf4j
public class UiViewServlet extends AbstractResourceServlet {

    private InitServletData initServletData = new InitServletData();


    public UiViewServlet() {
        super("y3tu-tool-ui");
    }

    @Override
    protected void beforeService(HttpServletRequest request, HttpServletResponse response,String uri,String path) throws IOException {
        // 权限校验
        String ip = request.getRemoteAddr();
        boolean security = BeanCache.getBean(AuthService.class).checkSecurity(initServletData,ip,path);
        if (!security) {
            response.sendRedirect("/401");
            return;
        }
        boolean checkLogin = checkLoginParam(request);
        if(!checkLogin){
            response.sendRedirect("/login");
            return;
        }
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
        initAuthEnv();
    }

    /**
     * 校验登录使用权限
     */
    private void initAuthEnv() {
        String paramUserName = getInitParameter(InitServletData.PARAM_NAME_USERNAME);
        if(StrUtil.isNotEmpty(paramUserName)){
            this.initServletData.setUsername(paramUserName);
        }
        String paramPassword = getInitParameter(InitServletData.PARAM_NAME_PASSWORD);
        if (StrUtil.isNotEmpty(paramPassword)) {
            this.initServletData.setPassword(paramPassword);
        }
        String paramAllow = getInitParameter(InitServletData.PARAM_NAME_ALLOW);
        if(StrUtil.isNotEmpty(paramAllow)){
            this.initServletData.setAllowList(parseStringToIP(paramAllow));
        }
        String paramDeny = getInitParameter(InitServletData.PARAM_NAME_DENY);
        if(StrUtil.isNotEmpty(paramDeny)){
            this.initServletData.setDenyList(parseStringToIP(paramDeny));
        }
    }

    private List<IPRange> parseStringToIP(String ipStr) {
        List<IPRange> ipList = new ArrayList<>();
        if (ipStr != null && ipStr.trim().length() != 0) {
            ipStr = ipStr.trim();
            String[] items = ipStr.split(",");

            for (String item : items) {
                if (item == null || item.length() == 0) {
                    continue;
                }
                IPRange ipRange = new IPRange(item);
                ipList.add(ipRange);
            }
        }
        return ipList;
    }

    /**
     * 校验登录用户名密码
     * @param request
     * @return
     */
    public boolean checkLoginParam(HttpServletRequest request) {
        String usernameParam = request.getParameter(InitServletData.PARAM_NAME_USERNAME);
        String passwordParam = request.getParameter(InitServletData.PARAM_NAME_PASSWORD);
        if(null ==  this.initServletData.getUsername() || null ==  this.initServletData.getPassword()){
            return false;
        } else if (this.initServletData.getUsername().equals(usernameParam) && this.initServletData.getPassword().equals(passwordParam)) {
            return true;
        }
        return false;
    }

}
