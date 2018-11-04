package com.y3tu.tool.http;

import com.y3tu.tool.http.callback.FileCallBack;

/**
 * 下载文件测试
 *
 * @author y3tu
 * @date 2018/10/30
 */
public class Test {

    public static void main(String args[]) {
        HttpUtil.get("https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg", new FileCallBack("/Users/yxy/work/", "meizi.jpg"));
    }
}
