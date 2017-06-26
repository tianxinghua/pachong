package com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotSpuSendDetailVo {

	@JsonIgnore
	private String brand;
	@JsonIgnore
	private String itemName;
	@JsonIgnore
	private String itemCode;
	@JsonIgnore
	private String studioCode;
	@JsonIgnore
	private String operator;
	@JsonIgnore
	private Date time;
	@JsonIgnore
	private byte arriveState;
	
	@JsonProperty("Brand")
	public String getBrand() {
		return brand;
	}
	@JsonProperty("Brand")
	public void setBrand(String brand) {
		this.brand = brand;
	}
	@JsonProperty("ItemName")
	public String getItemName() {
		return itemName;
	}
	@JsonProperty("ItemName")
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@JsonProperty("ItemCode")
	public String getItemCode() {
		return itemCode;
	}
	@JsonProperty("ItemCode")
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	@JsonProperty("StudioCode")
	public String getStudioCode() {
		return studioCode;
	}
	@JsonProperty("StudioCode")
	public void setStudioCode(String studioCode) {
		this.studioCode = studioCode;
	}
	@JsonProperty("Operator")
	public String getOperator() {
		return operator;
	}
	@JsonProperty("Operator")
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@JsonProperty("Time")
	public Date getTime() {
		return time;
	}
	@JsonProperty("Time")
	public void setTime(Date time) {
		this.time = time;
	}
	@JsonProperty("ArriveState")
	public byte getArriveState() {
		return arriveState;
	}
	@JsonProperty("ArriveState")
	public void setArriveState(byte arriveState) {
		this.arriveState = arriveState;
	}
	
}
