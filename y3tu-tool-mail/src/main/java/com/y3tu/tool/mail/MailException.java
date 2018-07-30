package com.y3tu.tool.mail;


import com.y3tu.tool.core.exception.ExceptionUtils;
import com.y3tu.tool.core.text.StringUtils;

/**
 * 邮件异常
 * @author xiaoleilu
 */
public class MailException extends RuntimeException{
	private static final long serialVersionUID = 8247610319171014183L;

	public MailException(Throwable e) {
		super(ExceptionUtils.getMessage(e), e);
	}
	
	public MailException(String message) {
		super(message);
	}
	
	public MailException(String messageTemplate, Object... params) {
		super(StringUtils.format(messageTemplate, params));
	}
	
	public MailException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public MailException(Throwable throwable, String messageTemplate, Object... params) {
		super(StringUtils.format(messageTemplate, params), throwable);
	}
}
