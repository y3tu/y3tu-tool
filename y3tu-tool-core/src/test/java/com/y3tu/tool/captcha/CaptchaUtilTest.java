package com.y3tu.tool.captcha;

import org.junit.Test;

public class CaptchaUtilTest {
	
	@Test
	public void createTest() {
		for(int i = 0; i < 1; i++) {
			CaptchaUtil.createShearCaptcha(320, 240);
		}
	}
}