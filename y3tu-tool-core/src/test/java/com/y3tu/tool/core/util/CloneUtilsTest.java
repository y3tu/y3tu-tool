package com.y3tu.tool.core.util;

import org.junit.Test;


/**
 * @author y3tu
 * @date 2018/9/8
 */
public class CloneUtilsTest {

    @Test
    public void deepClone() {
        Captcha captcha1 = new Captcha();
        captcha1.setCaptchaId("1");
        captcha1.setCode("code");
        System.out.println(captcha1.toString());
        Captcha captcha2 = CloneUtils.deepClone(captcha1);
        System.out.println(captcha2.toString());

    }

}