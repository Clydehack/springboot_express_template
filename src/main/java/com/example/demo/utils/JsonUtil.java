package com.example.demo.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @function:
 * 		json工具类
 * @author Clyde 2018-09-26
 */
public class JsonUtil {

	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	/**
	 * @function:
	 * 		正确应答
	 * @param state		调用状态
	 * @param msg		正常信息
	 * @param data		应答数据名称
	 * @param object	应答数据
	 * @return
	 */
	public static String resultJsonString(String data, Object object) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("state", "0");
			temp.put("msg", "success");
			if (data != null) {temp.put(data, object);}
			return mapper.writeValueAsString(temp);
		} catch (Exception e) {
			logger.error("resultJsonString", e);
		}
		return null;
	}
	
	/**
	 * @function:
	 * 		错误应答
	 * @param state		调用状态
	 * @param msg		失败信息
	 * @param errorCode	失败编码
	 * @return
	 */
	public static String resultJsonString(String msg, String errorCode) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("state", "1");
			temp.put("msg", msg);
			temp.put("errorCode", errorCode);
			return mapper.writeValueAsString(temp);
		} catch (Exception e) {
			logger.error("resultJsonString", e);
		}
		return null;
	}
}