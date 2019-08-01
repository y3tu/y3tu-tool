package com.y3tu.tool.core.http.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Resp {
    /**
     * 返回的编码
     * 200 正常  404 找不到页面
     */
    private int code;
    /**
     * 返回的数据
     */
    private Object data;

}
