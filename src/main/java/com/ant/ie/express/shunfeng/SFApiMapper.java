package com.ant.ie.express.shunfeng;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SFApiMapper {

	/* 取出系统设置的顺丰快递的预约时间段 */
	@Select("select time from sys_express_time_conf order by id")
	List<String> queryExpressTime();
}