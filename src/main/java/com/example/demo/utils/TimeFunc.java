package com.example.demo.utils;

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
	
	public static Date string2Date(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(date);
	}
}
