package com.y3tu.tool.core.http.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author y3tu
 */
@Data
public class IpLocate implements Serializable {

    private String retCode;

    private City result;
}

