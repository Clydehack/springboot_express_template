package com.ant.ie.express.jingdong;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 记录调用京东快递api的异常，需要人工处理
 */
@Mapper
public interface JdApiLogMapper {

	// 添加下单异常日志
	@Insert("insert into log_for_jd "
			+ "(exchange_id,order_id,business_state,api_state,content,err_msg,date_create) values "
			+ "(#{exchangeId},#{orderId},'1',#{apiState},#{content},#{errMsg},#{date})")
	Boolean placeAnOrder(@Param("exchangeId") String exchangeId, @Param("orderId") String orderId,
			@Param("apiState") String apiState, @Param("content") String content, 
			@Param("errMsg") String errMsg, @Param("date") String date);
	
	// 添加拦截异常日志
	@Insert("insert into log_for_jd "
			+ "(exchange_id,express_id,business_state,api_state,content,err_msg,date_create) values "
			+ "(#{exchangeId},#{expressId},'2',#{apiState},#{content},#{errMsg},#{date})")
	Boolean receiveOrder(@Param("exchangeId") String exchangeId, @Param("expressId") String expressId,
			@Param("apiState") String apiState, @Param("content") String content, 
			@Param("errMsg") String errMsg, @Param("date") String date);
		
	// 添加查询日志
	@Insert("insert into log_for_jd "
			+ "(exchange_id,express_id,business_state,api_state,content,err_msg,date_create) values "
			+ "(#{exchangeId},#{expressId},#{businessState},#{apiState},#{content},#{errMsg},#{date})")
	Boolean addquery(@Param("exchangeId") String exchangeId, @Param("expressId") String expressId,
			@Param("businessState") String businessState, @Param("apiState") String apiState, 
			@Param("content") String content, @Param("errMsg") String errMsg, @Param("date") String date);
}