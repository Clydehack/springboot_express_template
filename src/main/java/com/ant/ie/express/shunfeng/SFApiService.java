package com.ant.ie.express.shunfeng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ant.ie.model.ExpressTrace;
import com.ant.ie.utils.InvokeException;
import com.ant.ie.utils.StringUtil;
import com.ant.ie.utils.XmlUtil;
import com.sf.csim.express.service.CallExpressServiceTools;

/**
 * 顺丰快递服务
 */
@Service("sfApiService")
public class SFApiService {

	private Logger logger = LoggerFactory.getLogger(SFApiService.class);
	
//	private String custid = "7551234567";													// 顺丰测试月结号
//	private String custid = "7551234567";													// 顺丰测试月结号
	private String custid = "0210172147";													// 顺丰生产月结号
	private String clientCode = "CS_Pa7jz";													// 顾客编码
	private String checkWord = "QDe3cCzbBj5aj8ueFajAJcCMymUpwaKU";							// 校验码
	private String sfURL = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";		// 顺丰调用地址
	
	public static void main(String[] args) {
		SFApiService sfas = new SFApiService();
		try {
			/*
			sfas.sfOrderService("bdda4820saa1bbd684854257b4969df1", 
			"本子", 
			"2009-03-15 16:30:00", 
			"蚂蚁闲置", 
			"山东省青岛市黄岛区光谷软件园5号楼南区401", 
			"17553816111", 
			"潘常胜", 
			"山东省青岛市李沧区文昌路155号百度创新中心甲-18", 
			"15318734169");  */ // 下单测试
			/*sfas.sfOrderService("bdda48207ec1bbd684854257b4269df1", 
					"本子", 
					"2009-03-13 15:30:00", 
					"小草", 
					"上海市黄浦区建国西路120弄12号", 
					"17553816111",
					"蚂蚁闲置", 
					"山东省青岛市黄岛区光谷软件园5号楼南区401", 
					"17553816111"); */ // 下单测试
			/*
			sfas.sfOrderConfirmService("1dd567dd23"); // 取消测试
			 */
			
			List<ExpressTrace> le = sfas.sfRouteService("e831c87679981c5aac4d7d7ae8593497", "254266335887", "2");	// 查路由测试A
			System.out.println(le.toString());
			le = sfas.sfRouteService("0c2a7c2257224bad1cceafff26457e32", "254266336157", "2");	// 查路由测试B
			System.out.println(le.toString());
			le = sfas.sfRouteService("bdda4820saa1bbd684854257b4969df1", "254258572422", "2");	// 查路由测试 - 本子
			System.out.println(le.toString());
			Map<String, String> map = sfas.sfOrderSearchService("e831c87679981c5aac4d7d7ae8593497");
			System.out.println(map.get("orderid"));
			System.out.println(map.get("mailno"));
			System.out.println(map.get("origincode"));
			System.out.println(map.get("destcode"));
			System.out.println(map.get("filter_result"));
			System.out.println(map.get("remark"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 下单 - 客户系统向顺丰系统下达订单，为订单分配运单号 */
	@SuppressWarnings("static-access")
	public String sfOrderService(String orderId, String goodsDetail, String pickUpTime, 
			String senderName, String senderAddress, String senderMobile,
			String receiveName, String receiveAddress, String receiveMobile) throws InvokeException {
		String reqXML = "<Request service='OrderService' lang='zh-CN'><Head>" + clientCode + "</Head><Body>"
				+ "<Order orderid='" + orderId + "' "			// 平台订单号
				+ "cargo='" + goodsDetail + "' "				// 物品名称（物品详情）
				+ "remark='" + goodsDetail + "' "				// 物品备注
				+ "sendstarttime='" + pickUpTime + "' "			// 预约开始时间
				+ "j_company='" + senderName + "' "				// 寄件人公司名称
				+ "j_contact='" + senderName + "' "				// 寄件人名称
				+ "j_address='" + senderAddress + "' "			// 寄件人地址
				+ "j_tel='" + senderMobile + "' "				// 寄件人手机号
				+ "d_company='" + receiveName + "' "			// 收件人公司名称
				+ "d_contact='" + receiveName + "' "			// 收件人名称
				+ "d_address='" + receiveAddress + "' "			// 收件人地址
				+ "d_tel='" + receiveMobile + "' "				// 收件人手机号
				+ "custid='" + custid + "' "					// 顺丰月结卡号
				+ "express_type='2' "							// 1-顺丰标快 2-顺丰特惠 3-电商特惠
				+ "pay_method='3' "								// 1-寄方付 2-收方付 3-第三方付
				+ "is_docall='1' "								// 是否要求通过手持终端通知顺丰收派员收件 1-要求
				+ "parcel_quantity='1' >"						// 包裹数
//				+ "<AddedService name='COD' value='1.01' value1='7551234567'/>"	// 增值服务（等合同具体说明再定传什么）
				+ "</Order></Body></Request>";
        logger.info("向顺丰下单：" + reqXML);
		CallExpressServiceTools client = CallExpressServiceTools.getInstance();  
        String respXml= client.callSfExpressServiceByCSIM(sfURL, reqXML, clientCode, checkWord);

		if (!StringUtil.isNull(respXml)) {
			// TODO 下单未返回数据，先记录日志，然后人工下单
			logger.info("顺丰下单返回：" + respXml);					// TODO 出入参数全部记录入库
			Map<String, String> map = XmlUtil.xmlToMap(respXml);
			if("ERR".equals(map.get("Head"))) {
				//System.out.println(map.get("ERROR"));
				//System.out.println(map.get("code"));
				throw new InvokeException(map.get("code"), map.get("ERROR"));
			} else if ("OK".equals(map.get("Head"))) {
				//System.out.println(map.get("filter_result"));	// 1-人工确认 2-可收派 3-不可收派
				//System.out.println(map.get("orderid"));		// 客户订单号
				//System.out.println(map.get("mailno"));		// 顺丰运单号
				//System.out.println(map.get("origincode"));	// 原寄地区域代码
				//System.out.println(map.get("destcode"));		// 目的地区域代码
				return map.get("mailno");
			}
		}
		return respXml;
	}
	
	/** 取消订单 - 客户在发货前取消订单 */
	@SuppressWarnings("static-access")
	public boolean sfOrderConfirmService(String orderId) throws InvokeException {
		String reqXML = "<Request service='OrderConfirmService' lang='zh-CN'><Head>" + clientCode + "</Head><Body>"
				+ "<OrderConfirm orderid='" + orderId + "' dealtype='2'></OrderConfirm></Body></Request>";
        logger.info("向顺丰取消：" + reqXML);
		CallExpressServiceTools client = CallExpressServiceTools.getInstance();  
        String respXml= client.callSfExpressServiceByCSIM(sfURL, reqXML, clientCode, checkWord);

		if (!StringUtil.isNull(respXml)) {
			logger.info("顺丰取消返回：" + respXml);					// TODO 出入参数全部记录入库
			Map<String, String> map = XmlUtil.xmlToMap(respXml);
			if("ERR".equals(map.get("Head"))) {
				throw new InvokeException(map.get("code"), map.get("ERROR"));
			} else if ("OK".equals(map.get("Head"))) {
				return true;
			}
		}
		return false;
	}
	
	/** 订单结果查询（暂不使用） - 用于在未收到返回数据时，查询下订单（含筛选）接口客户订单当前的处理情况  */
	@SuppressWarnings("static-access")
	public Map<String, String> sfOrderSearchService(String orderId) throws InvokeException {
		String reqXML = "<Request service='OrderSearchService' lang='zh-CN'><Head>" + clientCode + "</Head><Body>"
				+ "<OrderSearch orderid='" + orderId + "'/></Body></Request>";
        logger.info("查询请求：" + reqXML);
		CallExpressServiceTools client = CallExpressServiceTools.getInstance();  
        String respXml = client.callSfExpressServiceByCSIM(sfURL, reqXML, clientCode, checkWord);

		if (!StringUtil.isNull(respXml)) {
			logger.info("查询结果返回：" + respXml);					// TODO 出入参数全部记录入库
			Map<String, String> map = XmlUtil.xmlToMap(respXml);
			if("ERR".equals(map.get("Head"))) {
				throw new InvokeException(map.get("code"), map.get("ERROR"));
			} else if ("OK".equals(map.get("Head"))) {
				return map;
			}
		}
		return null;
	}
	
	/** 路由查询   */
	@SuppressWarnings("static-access")
	public List<ExpressTrace> sfRouteService(String orderId, String expressId, String trackingType) throws InvokeException {
		List<ExpressTrace> le = new ArrayList<>();
		String trackingNumber = "";
		trackingType = "2";
		if("1".equals(trackingType)) {
			trackingNumber = expressId;
		} else {
			trackingNumber = orderId;
		}
		String reqXML = "<Request service='RouteService' lang='zh-CN'><Head>" + clientCode + "</Head><Body>"
				+ "<RouteRequest tracking_number='" + trackingNumber + "' "	// 查询号，如果有多个单号，以逗号分隔，如"123,124,125"。
				+ "tracking_type='" + trackingType + "' "					// 查询号类别 1-运单号查询 2-订单号查询
				+ "method_type='1' />"										// 1-标准路由查询 2-定制路由查询
				+ "</Body></Request>";
        logger.info("查询路由：" + reqXML);
		CallExpressServiceTools client = CallExpressServiceTools.getInstance();  
        String respXml= client.callSfExpressServiceByCSIM(sfURL, reqXML, clientCode, checkWord);

		if (!StringUtil.isNull(respXml)) {
			logger.info("查询路由返回：" + respXml);								// TODO 出入参数全部记录入库
			Map<String, String> map = XmlUtil.xmlToMap(respXml);
			if("ERR".equals(map.get("Head"))) {
				throw new InvokeException(map.get("code"), map.get("ERROR"));
			} else if ("OK".equals(map.get("Head"))) {
				// TODO 解析获取路由数据
				le = XmlUtil.xmlToListExpress(respXml);
				return le;
			}
		}
		return le;
	}
}