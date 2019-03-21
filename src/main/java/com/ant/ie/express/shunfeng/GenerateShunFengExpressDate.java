package com.ant.ie.express.shunfeng;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ant.ie.express.GenerateExpressDate;
import com.ant.ie.utils.InvokeException;

/**
 * 快递取件时间 - 顺丰
 */
@Service("generateShunFengExpressDate")
public class GenerateShunFengExpressDate implements GenerateExpressDate {

	@Autowired
	SFApiMapper sfApiMapper;
	
	private int[] intArr = {9,10,11,12,13,14,15,16};	// 用于节省点计算资源
	
	@Override
	public List<String> generateExpressDate() throws InvokeException {
		List<String> result = new ArrayList<>();
		// 先拿到当前日期
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.minusDays(-1);
		// 再拿到各种当前时间
        Date dateNow = new Date(System.currentTimeMillis());
        // 获得时针
        SimpleDateFormat sdFormatter = new SimpleDateFormat("HH");
        String retStr = sdFormatter.format(dateNow);
		int intNow = Integer.valueOf(retStr);
		//获得分针
		SimpleDateFormat sdFormatterMin = new SimpleDateFormat("mm");
		String retStrMin = sdFormatterMin.format(dateNow);
		int intNowMin = Integer.valueOf(retStrMin);
		// 取出设置的时间
		List<String> ls = sfApiMapper.queryExpressTime();
		if(ls == null || ls.isEmpty()) {throw new InvokeException("1001", "获取预约快递时间失败");}
		
		// 判断快递时间
        if(intNow < 6) {			// now小于6，预约今天和明天的快递
        	for(String str:ls) {
        		result.add(today + " " + str);
        	}
        	/*for(String str:ls) {
        		result.add(tomorrow + " " + str);
        	}*/
		} else if(intNow >= 13) {	// now大于13，预约明天的快递
			for(String str:ls) {
        		result.add(tomorrow + " " + str);
        	}
		} else {					// 大于等于6且小于等于13，预约时间是now+3以后的两小时时间段
			intNow = intNow + 3;
			if(intNowMin > 0) {		// 如果分针大于0分钟，预约下一个整点
				intNow = intNow + 1;
				if(intNow > 16) {	// 时针大于16，预约明天的快递
					for(String str:ls) {
		        		result.add(tomorrow + " " + str);
		        	}
					 return result;
				}
			}
			for(int i = 0;i < intArr.length;i++) {
				if(intNow == intArr[i]) {
					while(i<8) {
						result.add(today + " " + ls.get(i));
						i++;
					}
					break;
				}
			}
		}
        return result;
	}
}