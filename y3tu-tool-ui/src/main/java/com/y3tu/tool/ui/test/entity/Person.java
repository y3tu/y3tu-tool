package com.y3tu.tool.ui.test.entity;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author y3tu
 * @date 2019-04-08
 */
@Data
@AllArgsConstructor
public class Person implements Serializable {

    private long id;

    private String name;

    private Integer age;

    private String address;
}
