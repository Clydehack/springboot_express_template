package com.ant.ie.model;

/**
 * 根据快递单号查询到的单个快递信息，通常物流信息对象是 - List<ExpressTrace>
 */
public class ExpressTrace {
	
	private String opeTitle;	// 操作标题
	private String opeAddress;	// 操作地点
	private String opeRemark;	// 操作详情
	private String opeName;		// 操作人姓名
	private String opeTime;		// 操作时间
	private String waybillCode;	// 运单号
	private String courier;		// 配送员
	private String courierTel;	// 配送员电话
	
	public String getOpeTitle() {
		return opeTitle;
	}
	public void setOpeTitle(String opeTitle) {
		this.opeTitle = opeTitle;
	}
	public String getOpeRemark() {
		return opeRemark;
	}
	public void setOpeRemark(String opeRemark) {
		this.opeRemark = opeRemark;
	}
	public String getOpeName() {
		return opeName;
	}
	public void setOpeName(String opeName) {
		this.opeName = opeName;
	}
	public String getOpeTime() {
		return opeTime;
	}
	public void setOpeTime(String opeTime) {
		this.opeTime = opeTime;
	}
	public String getWaybillCode() {
		return waybillCode;
	}
	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	public String getCourierTel() {
		return courierTel;
	}
	public void setCourierTel(String courierTel) {
		this.courierTel = courierTel;
	}
	public String getOpeAddress() {
		return opeAddress;
	}
	public void setOpeAddress(String opeAddress) {
		this.opeAddress = opeAddress;
	}
}