package com.ant.ie.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 功能：
 * 		json工具类(Jackson)
 * 
 * 主要使用：
 * 		1. 接口统一应答信息  			convertCommonJsonString()
 * 		2. 将Object转换为json字符串  	toJson()
 * 		3. json字符串转为map对象		jsonToMap()
 */
public class JacksonUtil {
	
	private static Logger log = LoggerFactory.getLogger(JacksonUtil.class);

	/**
	 * 接口统一应答信息
	 * @param state		响应状态
	 * @param desc		响应信息
	 * @param code		业务编码/错误编码
	 * @param dataKey	业务数据
	 * @param data		业务数据实体
	 * @return
	 */
	public static String convertCommonJsonString(String state, String desc, String code, String dataKey, Object data){
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("state", state);
			temp.put("desc", desc);
			temp.put("code", code);
			if(data!=null) temp.put(dataKey, data);
			return mapper.writeValueAsString(temp);
		} catch (Exception e) {
			log.error("convertCommonJsonString", e);
		}
		return null;
	}
	
	/**
	 * 用于将map、list转换为json字符串
	 */
	public static String toJson(Object object) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/* json字符串转为map对象 */
	public static Map<String, String> jsonToMap(String parameter) throws Exception {
		ObjectMapper mapper = new ObjectMapper(); // 解析json
		JsonNode rootNode = mapper.readValue(parameter, JsonNode.class);
		Map<String, String> map = JacksonUtil.parseJsonNodeData(rootNode, "");
		return map;
	}
	
	/* 获得json里指定list节点下map里的指定String */
	public static String getMap(JsonNode jsonNode,int num,String name){
		String result = "";
		if(jsonNode.get(num).get(name) != null) {
			//result = jsonNode.get(num).get(name).toString(); toString方法获得的字符串是带""的
			result = jsonNode.get(num).get(name).asText();
		}
		return result;
	}
	
	
	
	/**
	 * 不带参数的正确应答
	 * @param state		应答状态码 0成功 1失败
	 * @param desc		应答描述
	 * @return
	 */
	public static String convertCommonJsonString(String state, String desc){
		try {
			return convertCommonJsonString(state, desc, null, null);
		} catch (Exception e) {
			log.error("convertCommonJsonString", e);
		}
		return null;
	}
	/**
	 * 带参数的正确应答
	 * @param state		应答状态码 0成功 1失败
	 * @param desc		应答描述
	 * @param dataKey	参数节点名，通用为data
	 * @param data		参数类型object
	 * @return
	 */
	public static String convertCommonJsonString(String state, String desc, String dataKey, Object data){
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("state", state);
			temp.put("desc", desc);
			if(data!=null) temp.put(dataKey, data);
			return mapper.writeValueAsString(temp);
		} catch (Exception e) {
			log.error("convertCommonJsonString", e);
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 正确应答信息(前端解析嵌套数据失败，临时更改传字符串类型，临时使用)
	 */
	public static String convertCommonJsonString(String state, String stateInfo, String dataKey, Object data, String dataKey1, Object data1){
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("state", state);
			temp.put("desc", stateInfo);
			if(data!=null) temp.put(dataKey, data);
			if(dataKey1!=null) temp.put(dataKey1, data1);
			return mapper.writeValueAsString(temp);
		} catch (Exception e) {
			log.error("convertCommonJsonString", e);
		}
		return null;
	}
	
	/**
	 * 解析通用格式json返回值并返回指定 json node 节点下的 键值数据
	 * 如果返回值出错则抛出异常
	 * @param jsonString 通用格式json返回值
	 * {"state":"0为成功1为失败",
	 *   "errorInfo":"",
	 *   ...
	 * }
	 * @return  Map&lt;String,String&gt;
	 */
	public static Map<String,String> parseCommonJsonData(String jsonString,String nodePath) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Map<String,String> data = new HashMap<String, String>();
		try {
			JsonNode rootNode = mapper.readValue(jsonString,JsonNode.class);
			String result = rootNode.path("state").asText();
			if(result.equals("1")){
				String desc = rootNode.path("errorInfo").asText();
				throw new Exception(desc);
			}
			data = parseJsonNodeData(rootNode,nodePath);
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	/**
	 * 解析指定 json node 节点下的 键值数据
	 * @param jsonString
	 * @return  Map&lt;String,String&gt;
	 */
	public static Map<String,String> parseJsonNodeData(String jsonString,String nodePath) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Map<String,String> data = new HashMap<String, String>();
		try {
			JsonNode rootNode = mapper.readValue(jsonString,JsonNode.class);
			data = parseJsonNodeData(rootNode,nodePath);
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	public static Map<String,String> parseJsonNodeData(JsonNode node,String nodePath) throws Exception{
		Map<String,String> data = new HashMap<String, String>();
		try {
			JsonNode temp = node;
			if(nodePath!=null && !nodePath.equals("")){
				String[] nodeNames = nodePath.split("/");
				for (int i = 0; i < nodeNames.length; i++) {
					temp = temp.get(nodeNames[i]);
					if(temp==null) throw new Exception("node("+nodeNames[i]+") is not exist");
				}
			}
			if(temp.isContainerNode()){
				Iterator<String> it = temp.fieldNames();
				while (it.hasNext()) {
					String name = it.next();
					JsonNode one = temp.path(name);
					if(one.isValueNode()){
						data.put(name, one.asText());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
}