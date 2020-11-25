package com.y3tu.tool.web.sql;

import lombok.Data;

import java.io.Serializable;

/**
 * 线程返回信息
 */
@Data
public class ThreadRes implements Serializable {

    private boolean success = false;

    private String msg;

}
