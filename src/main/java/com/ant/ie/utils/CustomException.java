package com.ant.ie.utils;

/**
 * @function:
 * 		自定义异常
 * @author Clyde 2018-09-26
 */
public class CustomException extends Exception {

	private static final long serialVersionUID = 1L;	// default id
	private String errorCode;							// custom errorCode
	
	public CustomException(String errorCode,String errorMessage){
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