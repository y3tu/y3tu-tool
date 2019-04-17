package com.y3tu.tool.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author y3tu
 * @date 2019-04-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

    private long id;

    private String name;

    private Integer age;

    private String address;
}
