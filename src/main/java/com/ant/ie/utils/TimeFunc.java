package com.ant.ie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期函数 用与输出时间相关数据
 * 
 * @author EDZ
 *
 */
public class TimeFunc {
	/**
	 * 获取当前时间的时间String值
	 * 
	 * @return
	 */
	public static String getTimeStr() {
		return getTimeStr(null);
	}

	public static String getTimeStr(Date date) {
		if (date == null)
			date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return df.format(date);
	}
	/**
	 * 将字符串转变为yyyy-MM-dd HH:mm:ss.SSS
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Date change2Date(String dStr) throws ParseException {
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = df.parse(dStr);
		} catch (ParseException e) {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = df.parse(dStr);
		}
		return date;
	}

	public static Date string2Date(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(date);
	}
	/**
	 * 将日期转换为yyyy-MM-dd HH:mm:ss 格式字符串
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String date2String(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
}
