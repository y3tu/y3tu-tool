package com.y3tu.tool.http;

import com.y3tu.tool.http.callback.FileCallBack;
import org.junit.Test;

/**
 * @author y3tu
 * @date 2019-03-20
 */
public class HttpUtilTest {

    @Test
    public void get(){
        HttpUtil.get("https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg", new FileCallBack("/Users/yxy/work/", "meizi.jpg"));
    }

}