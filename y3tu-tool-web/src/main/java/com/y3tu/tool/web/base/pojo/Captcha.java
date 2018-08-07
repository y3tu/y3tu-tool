package com.y3tu.tool.web.base.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Exrickx
 */
@Data
public class Captcha implements Serializable {

    /**
     * 验证码id
     */
    private String captchaId;

    /**
     * 验证码
     */
    private String code;
}
