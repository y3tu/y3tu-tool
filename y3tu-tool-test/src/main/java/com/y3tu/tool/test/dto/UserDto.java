package com.y3tu.tool.test.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author y3tu
 */
@Data
public class UserDto implements Serializable {
    @ExcelProperty("主键id")
    private long id;
    @ExcelProperty("名字")
    private String name;
    @ExcelProperty("年龄")
    private int age;

}
