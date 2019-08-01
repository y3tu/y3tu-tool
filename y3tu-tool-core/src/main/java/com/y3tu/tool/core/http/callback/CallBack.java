package com.y3tu.tool.core.http.callback;

import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

/**
 * 异步请求的回调函数
 */
public abstract class CallBack<T> {
    /**
     * 请求失败的时候调用
     *
     * @param call
     * @param e
     */
    public abstract void onFailure(Call call, IOException e);

    /**
     * 请求成功的时候返回
     *
     * @param call
     * @param response
     * @throws IOException
     */
    public abstract void onResponse(Call call, Response response) throws IOException;

    /**
     * 解析response，执行在子线程
     */
    public abstract T onParseResponse(Call call, Response response);
}
