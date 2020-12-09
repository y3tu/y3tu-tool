package com.y3tu.tool.web.util;

import lombok.Data;
import org.junit.Test;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 数据校验
 *
 * @author y3tu
 */
public class ValidatorUtilTest {

    @Test
    public void validate() {
        OrderCheck orderCheck = new OrderCheck();
        orderCheck.setOperator("1");
        ValidatorUtil.validate(orderCheck);
    }

    @Data
    class OrderCheck {
        @NotNull(message = "订单号不能为空")
        private String orderId;
        @Min(value = 1, message = "订单金额不能小于0")
        private Integer orderAmount;
        @NotNull(message = "创建人不能为空")
        private String operator;
        @NotNull(message = "操作时间不能为空")
        private String operatorTime;
    }
}