package com.ant.ie.model;

/**
 * 2C物流查询到的运单数据，根据快递单号查询到的单个快递信息，通常物流信息对象是 - List<ExpressTraceDto>
 */
public class ExpressTraceDto {

	private String operateDesc;		// 揽收任务分配
	private String operateMessage;	// 揽收任务已分配给P◆青岛灵山卫营业部
	private String operateTime;		// 1548899666000
	private String operateName;		// P◆青岛灵山卫营业部 - 张春涛
	private String waybillCode;		// VA49318929240
	
	public String getOperateDesc() {
		return operateDesc;
	}
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}
	public String getOperateMessage() {
		return operateMessage;
	}
	public void setOperateMessage(String operateMessage) {
		this.operateMessage = operateMessage;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getWaybillCode() {
		return waybillCode;
	}
	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}
}