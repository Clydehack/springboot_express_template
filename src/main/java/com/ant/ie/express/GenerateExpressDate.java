package com.ant.ie.express;

import java.util.List;

import com.ant.ie.utils.InvokeException;

/**
 * 生成预约快递时间
 */
public interface GenerateExpressDate {

	/** 生成当前，用户可选的，预约快递的时间 
	 * @throws InvokeException */
	public List<String> generateExpressDate() throws InvokeException;
}