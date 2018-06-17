package com.y3tu.tool.web.base.controller;


/**
 * Controller基类
 *
 * @author y3tu
 */
public class BaseController {
    /**
     * 重定向至地址 url
     *
     * @param url 请求地址
     * @return
     */
    protected String redirectTo(String url) {
        StringBuffer rto = new StringBuffer("redirect:");
        rto.append(url);
        return rto.toString();
    }



}
