package com.example.demo.intergration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.express.jingdong.JdApiService;
import com.example.demo.intergration.model.ExpressTrace;
import com.jd.open.api.sdk.JdException;

/**
 * 统一业务逻辑层,在这层判读调用哪家快递api
 */
@Service("expressService")
public class IntergrationExpressService {
	
	private Logger logger = LoggerFactory.getLogger(IntergrationExpressService.class);

	@Autowired
	JdApiService jdApiService;

	/** 下单 */
	public void addExpress(Map<String ,String> map,String orderId, 
			Date pickUpStartTime, Date pickUpEndTime, 
			String senderName, String senderAddress, String senderMobile, 
			String receiveName, String receiveAddress, String receiveMobile) {
		try {
			logger.info("开始下单");
			jdApiService.jingdongLdopWaybillReceive(map, orderId, 
					pickUpStartTime, pickUpEndTime, 
					senderName, senderAddress, senderMobile, 
					receiveName, receiveAddress, receiveMobile);
			logger.info("下单完毕");
		} catch (JdException e) {
			e.printStackTrace();
		}
	}
	
	/** 拦截取消 */
	public void cancelExpress(Map<String ,String> map, String deliveryId, String interceptReason) {
		try {
			logger.info("开始拦截");
			jdApiService.jingdongLdopReceiveOrderIntercept(map, deliveryId, interceptReason);
			logger.info("拦截完毕");
		} catch (JdException e) {
			e.printStackTrace();
		}
	}
	
	/** 查快递 */
	public List<ExpressTrace> queryExpress(String waybillCode) {
		List<ExpressTrace> list = new ArrayList<>();
		try {
			logger.info("开始查询");
			list = jdApiService.jingdongLdopReceiveTraceGet(waybillCode);
			logger.info("查询完毕");
		} catch (JdException e) {
			e.printStackTrace();
		}
		return list;
	}
}