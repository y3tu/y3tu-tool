package com.y3tu.tool.lowcode.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * jstat 键值对
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
public class JstatDto {
    private String key;
    private String value;
}
