package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @function:
 * 		时间工具类
 * @author Clyde
 */
public class TimeUtil {
	
	/**
	 * @function:
	 * 		生成当前时间戳字符串
	 * @param now	System.currentTimeMillis()
	 * @return
	 */
	public static String getNow(Long now) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(now);
		return sd.format(date);
	}
	
	/**
	 * @function:
	 * 		获得方法运行使用的时间长度
	 * @param start	System.currentTimeMillis()
	 * @param end	
	 * @return
	 */
	public static int getSpendTime(Long start, Long end) {
		return (int)(end-start);
	}
}