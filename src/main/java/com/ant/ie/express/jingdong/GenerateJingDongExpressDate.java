package com.ant.ie.express.jingdong;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ant.ie.express.GenerateExpressDate;

/**
 * 快递取件时间 - 京东
 */
@Service("generateJingDongExpressDate")
public class GenerateJingDongExpressDate implements GenerateExpressDate {

	@Override
	public List<String> generateExpressDate() {
		return null;
	}
}