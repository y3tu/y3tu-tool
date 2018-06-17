package com.y3tu.tool.web.base.pojo;

import java.util.HashMap;

/**
 * @author y3tu
 * @date 2018/1/10
 */
public class R extends HashMap<String, Object> {

    /**
     * 成功
     */
    private static final Integer SUCCESS = 200;
    /**
     * 警告
     */
    private static final Integer WARN = 1;
    /**
     * 异常 失败
     */
    private static final Integer FAIL = 500;

    public R() {
        put("code", SUCCESS);
        put("msg", "操作成功!");
    }


    /********************************************静态方法************************************************************/

    public static R ok() {
        R r = new R();
        return r.put("code", SUCCESS);
    }


    public static R ok(String msg) {
        R r = new R();
        return r.put("code", SUCCESS).put("msg", msg);
    }

    public static R ok(Object data) {
        R r = new R();
        return r.put("code", SUCCESS).put("data", data);
    }

    public static R ok(String msg, Object data) {
        R r = new R();
        return r.put("code", SUCCESS).put("msg", msg).put("data", data);
    }

    public static R warn(String msg) {
        R r = new R();
        return r.put("code", WARN).put("msg", msg);
    }

    public static R error(String msg) {
        R r = new R();
        return r.put("code", FAIL).put("msg", msg);
    }

    public static R error() {
        R r = new R();
        return r.put("code", FAIL).put("msg", "服务器内部异常，请联系管理员!");
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
