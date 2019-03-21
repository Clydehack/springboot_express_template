package com.ant.ie.express.jingdong;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ant.ie.config.E;
import com.ant.ie.model.ExpressTrace;
import com.ant.ie.model.ExpressTraceDto;
import com.ant.ie.utils.InvokeException;
import com.ant.ie.utils.TimeFunc;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.etms.OrderInfoOperateSaf.response.intercept.OrderInfoOperateResponse;
import com.jd.open.api.sdk.domain.etms.TraceQueryJsf.response.get.TraceDTO;
import com.jd.open.api.sdk.domain.etms.TraceQueryJsf.response.get.TraceQueryResultDTO;
import com.jd.open.api.sdk.domain.etms.Waybill2CTraceApi.response.Waybill2CTraceApi.BaseResult;
import com.jd.open.api.sdk.domain.etms.Waybill2CTraceApi.response.Waybill2CTraceApi.ExtTraceDto;
import com.jd.open.api.sdk.domain.etms.WaybillJosService.response.receive.WaybillResultInfoDTO;
import com.jd.open.api.sdk.request.etms.LdopMiddleWaybillWaybill2CTraceApiRequest;
import com.jd.open.api.sdk.request.etms.LdopReceiveOrderInterceptRequest;
import com.jd.open.api.sdk.request.etms.LdopReceiveTraceGetRequest;
import com.jd.open.api.sdk.request.etms.LdopWaybillReceiveRequest;
import com.jd.open.api.sdk.response.etms.LdopMiddleWaybillWaybill2CTraceApiResponse;
import com.jd.open.api.sdk.response.etms.LdopReceiveOrderInterceptResponse;
import com.jd.open.api.sdk.response.etms.LdopReceiveTraceGetResponse;
import com.jd.open.api.sdk.response.etms.LdopWaybillReceiveResponse;

/**
 * 封装京东快递api
 * 
 * 		返回的resultCode=100是调用接口成功标志
 */
@Component
public class JdApiService {

	private Logger logger = LoggerFactory.getLogger(JdApiService.class);
	
	// 场景一： 无服务器版(只配送，不入仓，使用此种方式，在浏览器调用，使用JOS申请过的账号才能成功获得token)
	String getToken = "https://oauth.jd.com/oauth/authorize?response_type=code&client_id=EDF176FE5CD8F4745CDF51941FAFFCE8&redirect_uri=urn:ietf:wg:oauth:2.0:oob&state=1212";
	String resultToken = "b9ca8ddb9efb41288dd97e000989fe06zgi5";	// 测试版24H有效，授权版1y有效
	
	// 场景二： 有服务器版(入仓)
	String getCodeThenGetToken = "https://oauth.jd.com/oauth/authorize?response_type=code&client_id=EDF176FE5CD8F4745CDF51941FAFFCE8&redirect_uri=www.mayixianzhi.com&state=1212";
	

	@Value("${jd_appid}")
	private String jd_appid;		// appKey
	@Value("${jd_secret}")
	private String jd_secret;		// appSecret
	@Value("${jd_url}")
	private String jd_url;			// 京东api地址
	@Value("${salePlat}")
	private String salePlat;		// 销售平台编码
	@Value("${customerCode}")
	private String customerCode;	// 商家编码
	@Value("${packageCount}")
	private Integer packageCount;	// 包裹数
	@Value("${weight}")
	private Double weight;			// 重量
	@Value("${vloumn}")
	private Double vloumn;			// 体积

	// 签名方式为 md5(appsecret + key+ value .... key +
	// value+appsecret)然后转大写字母，其中key、value对是除签名所有请求参数按key做的升序排列，value无需编码。

	/**
	 * 下单接口 - 京东物流接单接口
	 * 
	 * @param map				用于装载京东快递api返回的数据
	 * @param orderId			订单号
	 * @param pickUpStartTime	预约取件开始时间
	 * @param pickUpEndTime		预约取件结束时间
	 * @param senderName		寄件人姓名
	 * @param senderAddress		寄件人地址
	 * @param senderMobile		寄件人手机
	 * @param receiveName		收件人名称
	 * @param receiveAddress	收件人地址
	 * @param receiveMobile		收件人手机号
	 * @throws JdException		京东SDK自定义异常，内容未知
	 */
	public void jingdongLdopWaybillReceive(Map<String, String> map, String orderId, 
			Date pickUpStartTime, Date pickUpEndTime, String description
			, String senderName, String senderAddress, String senderMobile, 
			String receiveName, String receiveAddress, String receiveMobile) throws JdException {
		logger.info("京东快递，开始下单");
		// 调用京东快递sdk
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopWaybillReceiveRequest request = new LdopWaybillReceiveRequest();
		// 必填参数
		request.setSalePlat(salePlat); 				// 销售平台
		request.setCustomerCode(customerCode); 		// 商家编码
		request.setOrderId(orderId); 				// 订单号
		request.setSenderName(senderName); 			// 寄件人姓名
		request.setSenderAddress(senderAddress);	// 寄件人地址
		request.setSenderMobile(senderMobile); 		// 寄件人手机(寄件人电话、手机至少有一个不为空)
		request.setReceiveName(receiveName); 		// 收件人名称
		request.setReceiveAddress(receiveAddress);	// 收件人地址
		request.setReceiveMobile(receiveMobile);	// 收件人手机号(收件人电话、手机至少有一个不为空)
		request.setPackageCount(packageCount);  	// 包裹数(大于0，小于1000)
		request.setWeight(weight); 					// 重量(单位：kg，保留小数点后两位)
		request.setVloumn(vloumn); 					// 体积(单位：cm3，保留小数点后两位)
		// 非必填
		request.setDescription(description);		// 商品描述
		request.setPickUpStartTime(pickUpStartTime);// 预约取件开始时间
		request.setPickUpEndTime(pickUpEndTime);	// 预约取件结束时间
		/*
		request.setThrOrderId("jingdong"); 		// 销售平台订单号(例如京东订单号或天猫订单号等等。总长度不要超过100。如果有多个单号，用英文,间隔。例如：7898675,7898676)
		request.setSenderTel("jingdong"); 		// 寄件人电话
		request.setSenderPostcode("jingdong");	// 寄件人邮编
		request.setProvince("jingdong"); 		// 收件人省
		request.setCity("jingdong"); 			// 收件人市
		request.setCounty("jingdong"); 			// 收件人县
		request.setTown("jingdong"); 			// 收件人镇
		request.setProvinceId(0); 				// 收件人省编码
		request.setCityId(0); 					// 收件人市编码
		request.setCountyId(0); 				// 收件人县编码
		request.setTownId(0); 					// 收件人镇编码
		request.setSiteType(0); 				// 站点类型
		request.setSiteId(0); 					// 站点编码
		request.setSiteName("jingdong"); 		// 站点名称
		request.setReceiveTel("jingdong");		// 收件人电话
		request.setPostcode("jingdong"); 		// 收件人邮编
		request.setVloumLong(0.0); 				// 包裹长(单位：cm,保留小数点后两位)
		request.setVloumWidth(0.0);				// 包裹宽(单位：cm，保留小数点后两位)
		request.setVloumHeight(0.0); 			// 包裹高(单位：cm，保留小数点后两位)
		request.setDescription("jingdong");		// 商品描述
		request.setCollectionValue(0);			// 是否代收货款(是：1，否：0。不填或者超出范围，默认是0)
		request.setCollectionMoney(0.0);		// 代收货款金额(保留小数点后两位)
		request.setGuaranteeValue(0);			// 是否保价(是：1，否：0。不填或者超出范围，默认是0)
		request.setGuaranteeValueAmount(0.0);	// 保价金额(保留小数点后两位)
		request.setSignReturn(0);				// 签单返还(签单返还类型：0-不返单，1-普通返单，2-校验身份返单，3-电子签返单，4-电子返单+普通返单)
		request.setAging(1);					// 时效(普通：1，工作日：2，非工作日：3，晚间：4。O2O一小时达：5。O2O定时达：6。不填或者超出范围，默认是1)
		request.setTransType(1);				// 运输类型(陆运：1，航空：2。不填或者超出范围，默认是1)
		request.setRemark("jingdong");			// 运单备注（不超过20个字）,打印面单时备注内容也会显示在快递面单上
		request.setGoodsType(1);				// 配送业务类型（ 1:普通，3:填仓，4:特配，6:控温，7:冷藏，8:冷冻，9:深冷）默认是1
		request.setOrderType(0);				// 运单类型。(普通外单：0，O2O外单：1)默认为0
		request.setOrderSendTime("jingdong");	// 预约配送时间（格式：yyyy-MM-dd HH:mm:ss。例如：2014-09-18 08:30:00）
		request.setWarehouseCode("jingdong");	// 发货仓编码
		request.setAreaProvId(0);				// 接货省ID
		request.setAreaCityId(0);				// 接货市ID
		request.setShipmentStartTime(null);		// 配送起始时间
		request.setShipmentEndTime(null);		// 配送结束时间
		request.setIdNumber("jingdong");		// 身份证号
		request.setAddedService("jingdong");	// 扩展服务
		request.setSenderCompany("jingdong"); 	// 寄件人公司
		request.setReceiveCompany("jingdong");	// 收件人公司
		request.setGoods("jingdong");			// 托寄物名称（为避免托运物品在铁路、航空安检中被扣押，请务必下传商品类目或名称，例如：服装、3C等）
		request.setGoodsCount(0);				// 寄托物数量
		request.setPromiseTimeType(0);			// 产品类型（5：同城即日）
		request.setFreight(0.0);				// 运费
		*/
		// 京东快递api应答
		LdopWaybillReceiveResponse response = client.execute(request);
		if(response == null) {return;}
		WaybillResultInfoDTO resultDTO = response.getReceiveorderinfoResult();
		if(resultDTO == null) {return;}
		// 装载应答
		map.put("resultCode", ""+resultDTO.getResultCode());	// 结果编码/错误编码
		map.put("resultMsg", resultDTO.getResultMessage());		// 结果描述/错误描述
		map.put("deliveryId", resultDTO.getDeliveryId());		// 运单号
		//logger.error("京东物流接单接口异常：code：" + e.getErrCode() + ",msg：" + e.getErrMsg());
		logger.info("京东快递，下单结束");
	}

	/**
	 * 运单取消(拦截) - 此接口bu不一定拦截成功，拦截失败需要线下拦截
	 * 
	 * @param map				用于装载京东快递api返回的数据
	 * @param deliveryId		运单号
	 * @param interceptReason	拦截原因
	 * @throws JdException		京东SDK自定义异常
	 */
	public void jingdongLdopReceiveOrderIntercept(Map<String, String> map, String deliveryId, String interceptReason) throws JdException {
		logger.info("京东快递，拦截开始");
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopReceiveOrderInterceptRequest request = new LdopReceiveOrderInterceptRequest();
		request.setVendorCode(customerCode);				// 商家编码
		request.setDeliveryId(deliveryId);					// 运单号
		request.setInterceptReason(interceptReason);		// 拦截原因
		// 京东快递api应答
		LdopReceiveOrderInterceptResponse response = client.execute(request);
		if(response == null) {return;}
		OrderInfoOperateResponse resultDTO = response.getResultInfo();
		if(resultDTO == null) {return;}
		map.put("resultCode", ""+resultDTO.getStateCode());	// 结果编码/错误编码 - 处理状态码（100为发送拦截成功，其他为失败）
		map.put("resultMsg", resultDTO.getStateMessage());	// 结果描述/错误描述 - 处理结果
		//logger.error("京东运单拦截取消接口异常：code：" + e.getErrCode() + ",msg：" + e.getErrMsg());
		logger.info("京东快递，拦截结束");
	}
	
	/**
	 * 物流查询 - 2C全程物流跟踪接口 （to C 个人用户运单查询接口）
	 * 
	 * @param map				用于装载京东快递api返回的数据
	 * @param waybillCode		运单号
	 * @throws JdException		京东SDK自定义异常
	 * @throws InvokeException 
	 * @throws ParseException 
	 */
	public List<ExpressTraceDto> jingdongLdopMiddleWaybillWaybill2CTraceApi(String waybillCode) throws JdException, InvokeException, ParseException {
		logger.info("京东快递，2C全程物流查快递开始");
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopMiddleWaybillWaybill2CTraceApiRequest request=new LdopMiddleWaybillWaybill2CTraceApiRequest();
		request.setTradeCode(customerCode);		// 商家编码
		request.setWaybillCode(waybillCode);	// 运单号
		LdopMiddleWaybillWaybill2CTraceApiResponse response = client.execute(request);
		if(response == null) {throw new InvokeException("JD001",E.JD001);}
		BaseResult baseResult = response.getBaseResult();
		if(baseResult == null) {throw new InvokeException("JD001",E.JD001);}
		//if(baseResult.getCode() != 100) {throw new JdException(baseResult.getStatusMessage(), "" + baseResult.getStatusCode());}
		List<ExtTraceDto> listExtTraceDto = baseResult.getData();			// 京东的物流信息
		List<ExpressTraceDto> listExpressTraceDto = new ArrayList<>();		// 我方的物流信息对象
		if(listExtTraceDto != null) {										// 如果现在有快递信息了就把京东的物流信息转换成我方的物流信息对象
			// 装载物流信息
			for(ExtTraceDto extTraceDto:listExtTraceDto) {
				ExpressTraceDto expressTraceDto = new ExpressTraceDto();
				expressTraceDto.setOperateDesc(extTraceDto.getOperateDesc());						// 操作标题
				expressTraceDto.setOperateMessage(extTraceDto.getOperateMessage());
				expressTraceDto.setOperateName(extTraceDto.getOperateName());
				expressTraceDto.setOperateTime(TimeFunc.date2String(extTraceDto.getOperateTime()));
				expressTraceDto.setWaybillCode(extTraceDto.getWaybillCode());
				listExpressTraceDto.add(expressTraceDto);
			}
		}
		logger.info("京东快递，2C全程物流查快递结束");
		return listExpressTraceDto;
	}
	
	/**
	 * 物流查询 - 查询物流跟踪消息
	 * 
	 * @param map				用于装载京东快递api返回的数据
	 * @param waybillCode		运单号
	 * @throws JdException		京东SDK自定义异常
	 * @throws InvokeException 
	 */
	public List<ExpressTrace> jingdongLdopReceiveTraceGet(String waybillCode) throws JdException, InvokeException {
		logger.info("京东快递，查快递开始");
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopReceiveTraceGetRequest request=new LdopReceiveTraceGetRequest();
		request.setCustomerCode(customerCode);	// 商家编码
		request.setWaybillCode(waybillCode);	// 运单号
		// 京东快递api应答
		LdopReceiveTraceGetResponse response = client.execute(request);
		if(response == null) {throw new InvokeException("JD001",E.JD001);}
		TraceQueryResultDTO resultDTO = response.getQuerytraceResult();
		if(resultDTO == null) {throw new InvokeException("JD001",E.JD001);}
		if(resultDTO.getCode() != 100) {throw new JdException(resultDTO.getMesssage(), "" + resultDTO.getCode());}
		List<TraceDTO> listTraceDTO = resultDTO.getData();				// 京东的物流信息
		List<ExpressTrace> listExpressTrace = new ArrayList<>();		// 我方的物流信息对象
		if(listTraceDTO != null) {										// 如果现在有快递信息了就把京东的物流信息转换成我方的物流信息对象
			// 装载物流信息
			for(TraceDTO traceDTO:listTraceDTO) {
				ExpressTrace expressTrace = new ExpressTrace();
				expressTrace.setOpeTitle(traceDTO.getOpeTitle());		// 操作标题
				expressTrace.setOpeRemark(traceDTO.getOpeRemark());		// 操作详情
				expressTrace.setOpeName(traceDTO.getOpeName());			// 操作人姓名
				expressTrace.setOpeTime(traceDTO.getOpeTime());			// 操作时间
				expressTrace.setWaybillCode(traceDTO.getWaybillCode());	// 运单号
				expressTrace.setCourier(traceDTO.getCourier());			// 配送员
				expressTrace.setCourierTel(traceDTO.getCourierTel());	// 配送员电话
				listExpressTrace.add(expressTrace);
			}
		}
		logger.info("京东快递，查快递结束");
		return listExpressTrace;
	}

	/**
	 * 异常单审核 - 对需操作审核的订单进行再投，退货或报废
	 * 
	 * @param map				用于装载京东快递api返回的数据
	 * @param deliveryId		运单号
	 * @param interceptReason	返回描述
	 * @param interceptReason	异常处理结果（1：再投，2：退回，3：站点报废）
	 * @throws JdException		京东SDK自定义异常
	
	public void jingdongLdopAbnormalApproval(Map<String, Object> map, String deliveryId) {
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopAbnormalApprovalRequest request = new LdopAbnormalApprovalRequest();
		request.setCustomerCode(customerCode);		// 商家编码
		request.setDeliveryId(deliveryId);			// 运单号
		request.setResponseComment( "jingdong" );	// 返回描述
		request.setType( 123 );						// 异常处理结果（1：再投，2：退回，3：站点报废）
		// 京东快递api应答
		try {
			LdopAbnormalApprovalResponse response = client.execute(request);
			if(response == null) {return;}
			ResponseDTO resultDTO = response.getApprovalResult();
			if(resultDTO == null) {return;}
			map.put("resultCode", resultDTO.getStatusCode());	// 结果编码/错误编码
			map.put("resultMsg", resultDTO.getStatusMessage());	// 结果描述/错误描述
		} catch (JdException e) {
			logger.error("京东查询物流跟踪消息接口异常：code：" + e.getErrCode() + ",msg：" + e.getErrMsg());
		}
	}
	 */

	/**
	 * 重量包裹数查询接口
	 * @throws JdException 
	
	public void jingdongLdopWaybillQuery(Map<String, Object> map, String deliveryId) throws JdException {
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopWaybillQueryRequest request=new LdopWaybillQueryRequest();
		request.setDeliveryId(deliveryId);			// 运单号
		request.setCustomerCode(customerCode);		// 商家编码
		// 京东快递api应答
		LdopWaybillQueryResponse response=client.execute(request);
		response.getResultInfo();
	}
	 */
	/**
	 * (待定)
	 * 取件单下单 - 像是到商家取件的接口 - jingdong.ldop.receive.pickuporder.receive
	 * 
	 * @throws JdException
	
	public void jingdongLdopReceivePickuporderReceive() throws JdException {

		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);

		LdopReceivePickuporderReceiveRequest request = new LdopReceivePickuporderReceiveRequest();
		// 必填参数
		request.setPickupAddress("jingdong"); 		// 取件地址
		request.setPickupName("jingdong"); 			// 取件联系人
		request.setPickupTel("jingdong"); 			// 取件联系电话
		request.setCustomerTel("jingdong"); 		// 商家联系电话
		request.setCustomerCode("jingdong"); 		// 商家编码
		request.setBackAddress("jingdong"); 		// 退货地址
		request.setCustomerContract("jingdong"); 	// 商家联系人
		request.setDesp("jingdong"); 				// 取件描述
		request.setOrderId("jingdong"); 			// 订单号
		request.setWeight(null); 					// 重量 可统一传默认值，仅作参考，但不能太大
		request.setRemark("jingdong"); 				// 备注
		request.setVolume(null); // 体积 可统一传默认值，仅作参考，但不能太大
		request.setProductName("jingdong,yanfa,pop"); // 商品名字
		request.setProductCount("0,234,345"); // 商品数量
		// 非必填
		request.setValueAddService("jingdong"); // 增值服务
		request.setGuaranteeValue(true); // 是否保价
		request.setGuaranteeValueAmount(null); // 保价金额
		request.setPickupStartTime(null); // 开始取件时间
		request.setPickupEndTime(null); // 结束取件时间
		request.setProductId(""); // 商品SKU(最小存货单位)
		request.setSnCode(""); // 商品条码，选择校验条码时，必填

		LdopReceivePickuporderReceiveResponse response = client.execute(request);
	}
	 */
	/**
	 * 查询拒收再投单
	 * 
	 * @param map
	 * @throws JdException
	
	public void jingdongLdopAbnormalGet(Map<String, Object> map) throws JdException {
		JdClient client = new DefaultJdClient(jd_url, resultToken, jd_appid, jd_secret);
		LdopAbnormalGetRequest request=new LdopAbnormalGetRequest();
		request.setCustomerCode(customerCode);		// 商家编码
		// 京东快递api应答
		LdopAbnormalGetResponse response=client.execute(request);
		if(response == null) {return;}
		// import com.jd.open.api.sdk.domain.etms.AbnormalOrderRequestApi.response.get.ResponseDTO;
		ResponseDTO resultDTO = response.getQuerybyconditionResult();
		if(resultDTO == null) {return;}
		map.put("resultCode", resultDTO.getStatusCode());	// 结果编码/错误编码
		map.put("resultMsg", resultDTO.getStatusMessage());	// 结果描述/错误描述
	}
	 */
}