package com.y3tu.tool.core.http.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * ip信息-城市
 *
 * @author y3tu
 */
@Data
public class City implements Serializable {

    String country;

    String province;

    String city;
}
