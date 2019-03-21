package com.ant.ie.config;

/**
 * 错误码类
 * 
 * @author EDZ
 *
 */
public class E {
	public static StringBuffer output;
	// 系统错误
	public final static String SYS5001 = "系统繁忙";
	public final static String SYS5002 = "未查到系统设置的快递预约时间";

	// 物品错误
	public final static String GOOD2001 = "用户编辑物品失败";
	public final static String GOOD2002 = "物品下架失败";
	public final static String GOOD2003 = "物品发布失败，请联系管理员";
	public final static String GOOD2004 = "查询物品详情失败";
	public final static String GOOD2005 = "点赞失败";
	public final static String GOOD2006 = "取消点赞失败";
	public final static String GOOD2007 = "收藏失败";
	public final static String GOOD2008 = "取消收藏失败";
	public final static String GOOD2009 = "查询物品详情的快递费和平台鉴定费失败";
	public final static String GOOD2010 = "您点击的太快了，请1分钟以后再操作";

	// 主页错误
	public final static String HOME3001 = "轮播图地址为空，查询轮播图失败";

	// 订单错误
	public final static String ORDER4001 = "你的物品处于异常状态";
	public final static String ORDER4002 = "自己的物品未处于上架状态或者已锁定";
	public final static String ORDER4003 = "想要交换物品未处上架状态或者正在交换别的物品";
	public final static String ORDER4004 = "对方的物品未处于锁定状态";
	public final static String ORDER4005 = "自己的物品未处于锁定状态或未上架";
	public final static String ORDER4006 = "你的物品处于异常状态";
	public final static String ORDER4007 = "该订单已经被取消";
	public final static String ORDER4008 = "更新订单数据库失败";
	public final static String ORDER4009 = "订单状态异常";
	public final static String ORDER4010 = "对方正在支付中，请等待对方操作完成再取消订单";
	public final static String ORDER4011 = "查询平台费用失败";
	public final static String ORDER4012 = "未查到此id的取件地址";
	public final static String ORDER4013 = "发起方订单不存在";
	public final static String ORDER4014 = "被发起方订单不存在";
	public final static String ORDER4015 = "同步双方预约时间失败，请重试一次";
	public final static String ORDER4016 = "确认收货失败";
	public final static String ORDER4017 = "已换出物品下架失败";
	public final static String ORDER4018 = "确认收货加分失败";
	public final static String ORDER4019 = "评价信息未成功传递";
	public final static String ORDER4020 = "物品状态异常";

	// 用户错误
	public final static String USER5001 = "意见收集失败,数据库更新失败";
	public final static String USER5002 = "更新session_key,数据库更新失败";
	public final static String USER5003 = "存储session_key,数据库插入失败";
	public final static String USER5004 = "解密微信数据，获取unionId失败";
	// 打印日志错误错误
	public final static String LOG5001 = "04";

	// 微信错误
	public final static String WX001 = "微信返回空值";
	public final static String WX002 = "从微信获取openId失败";
	public final static String WX003 = "微信预支付失败";
	public final static String WX004 = "微信退款失败";
	public final static String WX005 = "从微信获取session_key失败";

	// 京东错误
	public final static String JD001 = "京东快递未返回物流信息";
	public final static String JD002 = "预约京东快递未成功，请重试一次";
	public final static String JD003 = "发起方已进入待发货状态，但是预约京东快递失败";
	public final static String JD004 = "被发起方已进入待发货状态，但是预约京东快递失败";
	public final static String JD005 = "订单有误";

	/** 必须参数未输入错误 */
	public static String E1001(String param) {
		return param + "必须输入";
	}
	/** 必须参数请输入 */
	public static String E1005(String param) {
		return "请输入" + param;
	}
	/** 必须文件请上传 */
	public static String E1006(String param) {
		return "请上传" + param;
	}
	/** 必须参数请选择 */
	public static String E1007(String param) {
		return "请选择" + param;
	}
	
	/** 必须参数值数值不在有效值范围内 */
	public static String E1002(String param) {
		return "参数" + param + "的值是无效值";
	}

	/** 查询出来的结果为空 */
	public static String E1003 = "查询出来的结果集为空";

	/** 参数不存在 */
	public static String E1004(String param) {
		return param + "不存在";
	}
}
