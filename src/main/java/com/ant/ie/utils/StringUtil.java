package com.ant.ie.utils;

import java.util.Date;
import java.util.UUID;

public class StringUtil {

	/**
	 * 验证字符串类型是否为空
	 * 
	 * @param inputString 空值返回true，非空返回false
	 * @return
	 */
	public static Boolean isNull(String inputString) {
		if (inputString == null || "".equals(inputString) || "null".equals(inputString)
				|| "undefined".equals(inputString)) {
			return true;
		}
		return false;
	}

	public static Boolean isNull(Date date) {
		if (date == null) {
			return true;
		}
		return false;
	}

	public static boolean isNull(Integer param) {
		if (param == null) {
			return true;
		}
		return false;
	}

	/**
	 * 获取没有横杠的uuid
	 * 
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}

	/**
	 * 检验字符是否是正整数
	 * 
	 * @param pageStr
	 * @return
	 */
	public static boolean isPosInt(String str) {
		if (str == null || "".equals(str.trim()))
			return false;
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 隐藏姓名
	 * 
	 * @param name
	 * @return
	 */
	public static String hideName(String name) {
		String str = "**";
		StringBuilder sb = new StringBuilder(name);
		int i = 0;
		if (name.length() == 2) {
			i = 2;
		} else {
			i = name.length() - 1;
		}
		sb.replace(1, i, str);
		return sb.toString();
	}

	/**
	 * 隐藏手机号中间四位
	 * 
	 * @param phone
	 * @return
	 */
	public static String hidePhoneNum(String phone) {
		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
	}

	/**
	 * 隐藏快递地址
	 * 
	 * @param address
	 * @return
	 */
	public static String hideAddress(String address) {
		String str = "****";
		StringBuilder sb = new StringBuilder(address);
		int i = 0;
		int j = 0;
		if (address.length() <= 9) {
			i = 9;
		} else {
			i = address.length() - 3;
		}
		if (address.length() < 6) {
			j = address.length() - address.length() + 1;
		} else {
			j = 6;
		}
		sb.replace(j, i, str);
		return sb.toString();
	}

}