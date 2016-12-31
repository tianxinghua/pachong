package com.shangpin.ephub.product.business.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicDataResponse <T> {

	public BasicDataResponse() {
		super();
	}
	
	public BasicDataResponse(T resDatas, boolean isSuccess, String resMsg) {
		super();
		this.resDatas = resDatas;
		this.isSuccess = isSuccess;
		this.resMsg = resMsg;
	}

	public BasicDataResponse(T resDatas, boolean isSuccess) {
		super();
		this.resDatas = resDatas;
		this.isSuccess = isSuccess;
	}

	public BasicDataResponse(T resDatas) {
		super();
		this.resDatas = resDatas;
	}

	@JsonIgnore
	private T resDatas;
	@JsonIgnore
	private boolean isSuccess;
	@JsonIgnore
	private String resMsg;
	
	@JsonProperty("ResDatas")
	public T getResDatas(){
		return this.resDatas;
	}
	@JsonProperty("ResDatas")
	public void setResDatas(T resDatas){
		this.resDatas = resDatas;
	}
	@JsonProperty("IsSuccess")
	public boolean getIsSuccess(){
		return this.isSuccess;
	}
	@JsonProperty("IsSuccess")
	public void setIsSuccess(boolean isSuccess){
		this.isSuccess = isSuccess;
	}
	@JsonProperty("ResMsg")
	public String getResMsg(){
		return this.resMsg;
	}
	@JsonProperty("ResMsg")
	public void setResMsg(String resMsg){
		this.resMsg = resMsg;
	}
	
}
