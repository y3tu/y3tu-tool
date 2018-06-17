package com.y3tu.tool.web.base.entity;

import java.io.Serializable;

/**
 * 实体基类
 * @author y3tu
 */
public abstract class BaseEntity<T> implements Serializable{
    protected abstract Serializable pkVal();
}
