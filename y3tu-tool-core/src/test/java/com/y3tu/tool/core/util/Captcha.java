package com.y3tu.tool.core.util;


import java.io.Serializable;

/**
 * @author
 */
public class Captcha implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 验证码id
     */
    private String captchaId;

    /**
     * 验证码
     */
    private String code;

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
