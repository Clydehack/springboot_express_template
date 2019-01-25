package com.example.demo.intergration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.intergration.model.ExpressTrace;
import com.example.demo.utils.JacksonUtil;
import com.example.demo.utils.TimeFunc;

/**
 * 测试快递业务统一入口
 */
@RestController
@RequestMapping("/express")
public class IntergrationExpressController {

	private Logger logger = LoggerFactory.getLogger(IntergrationExpressController.class);

	private void setHeader(HttpServletResponse response) { // 跨域访问
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setDateHeader("Expires", 0);
	}
	
	@Autowired
	IntergrationExpressService expressService;
	
	/** 下单 */
	@RequestMapping(value = "/addExpress", method = { RequestMethod.POST })
	public String addExpress(HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		setHeader(response);
		try {
			/* 解析校验参数 */
			//String parameter = request.getParameter("parameter");
			//Map<String, String> map = JacksonUtil.jsonToMap(parameter);
			// if (StringUtil.isNull(map.get("userId"))) 	{throw new InvokeException("1001", E.E1001("userId"));}

			String orderId = request.getParameter("orderId"); 					// 订单号
			String pickUpStartTime = request.getParameter("pickUpStartTime"); 	// 预约取件开始时间
			String pickUpEndTime = request.getParameter("pickUpEndTime"); 		// 预约取件结束时间
			String senderName = request.getParameter("senderName"); 			// 寄件人姓名
			String senderAddress = request.getParameter("senderAddress"); 		// 寄件人地址
			String senderMobile = request.getParameter("senderMobile"); 		// 寄件人手机(寄件人电话、手机至少有一个不为空)
			String receiveName = request.getParameter("receiveName"); 			// 收件人名称
			String receiveAddress = request.getParameter("receiveAddress"); 	// 收件人地址
			String receiveMobile = request.getParameter("receiveMobile"); 		// 收件人手机号(收件人电话、手机至少有一个不为空)
			
			Map<String ,String> map = new HashMap<>();
			expressService.addExpress(map, orderId, 
					TimeFunc.string2Date(pickUpStartTime), TimeFunc.string2Date(pickUpEndTime), 
					senderName, senderAddress, senderMobile, 
					receiveName, receiveAddress, receiveMobile);
			
			result = JacksonUtil.convertCommonJsonString("0", "已开始下单", null, "data", map);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = JacksonUtil.convertCommonJsonString("1", "系统错误", "9999", null, null);
		}
		return result;
	}
	
	/** 拦截取消 */
	@RequestMapping(value = "/cancelExpress", method = { RequestMethod.POST })
	public String cancelExpress(HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		setHeader(response);
		try {
			/* 解析校验参数 */
			//String parameter = request.getParameter("parameter");
			//Map<String, String> map = JacksonUtil.jsonToMap(parameter);
			// if (StringUtil.isNull(map.get("userId"))) 	{throw new InvokeException("1001", E.E1001("userId"));}
			String deliveryId = request.getParameter("deliveryId");
			String interceptReason = request.getParameter("interceptReason");
			
			Map<String ,String> map = new HashMap<>();
			expressService.cancelExpress(map, deliveryId, interceptReason);
			
			result = JacksonUtil.convertCommonJsonString("0", "已开始拦截", null, "data", map);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = JacksonUtil.convertCommonJsonString("1", "系统错误", "9999", null, null);
		}
		return result;
	}
	
	/** 查快递 */
	@RequestMapping(value = "/queryExpress", method = { RequestMethod.POST })
	public String queryExpress(HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		setHeader(response);
		try {
			/* 解析校验参数 */
			//String parameter = request.getParameter("parameter");
			//Map<String, String> map = JacksonUtil.jsonToMap(parameter);
			// if (StringUtil.isNull(map.get("waybillCode"))) 	{throw new InvokeException("1001", E.E1001("waybillCode"));}
			String waybillCode = request.getParameter("waybillCode");

			List<ExpressTrace> list = expressService.queryExpress(waybillCode);
			
			result = JacksonUtil.convertCommonJsonString("0", "查询成功", null, "data", list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = JacksonUtil.convertCommonJsonString("1", "系统错误", "9999", null, null);
		}
		return result;
	}
	
}