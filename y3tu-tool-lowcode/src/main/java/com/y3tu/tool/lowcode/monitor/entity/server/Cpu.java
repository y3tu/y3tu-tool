package com.y3tu.tool.lowcode.monitor.entity.server;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * cpu相关信息
 *
 * @author y3tu
 */
@Data
public class Cpu {
    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private BigDecimal total;

    /**
     * CPU系统使用率
     */
    private BigDecimal sys;

    /**
     * CPU用户使用率
     */
    private BigDecimal used;

    /**
     * CPU当前等待率
     */
    private BigDecimal wait;

    /**
     * CPU当前空闲率
     */
    private BigDecimal free;

    public BigDecimal getTotal() {
        return NumberUtil.round(NumberUtil.mul(total, 100), 2);
    }

    public BigDecimal getSys() {
        return NumberUtil.round(NumberUtil.mul(NumberUtil.div(sys, total), 100), 2);
    }

    public BigDecimal getUsed() {
        return NumberUtil.round(NumberUtil.mul(NumberUtil.div(used, total), 100), 2);
    }

    public BigDecimal getWait() {
        return NumberUtil.round(NumberUtil.mul(NumberUtil.div(wait, total), 100), 2);
    }

    public BigDecimal getFree() {
        return NumberUtil.round(NumberUtil.mul(NumberUtil.div(free, total), 100), 2);
    }

}
