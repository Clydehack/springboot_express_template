package com.ant.ie.utils;

/**
 * 功能：
 * 		自定义异常类
 * 		抛出自定义的错误信息和错误编码
 */
public class InvokeException extends Exception {
	
	private static final long serialVersionUID = 7007028258897592959L;
	
	private String errorCode;
	
	public InvokeException(String errorCode,String errorMessage){
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}