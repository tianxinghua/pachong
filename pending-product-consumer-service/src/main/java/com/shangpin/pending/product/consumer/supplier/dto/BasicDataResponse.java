package com.shangpin.pending.product.consumer.supplier.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BasicDataResponse <T> {

	public BasicDataResponse() {
		super();
	}
	
	public BasicDataResponse(List<T> resDatas, boolean isSuccess, String resMsg) {
		super();
		this.resDatas = resDatas;
		this.isSuccess = isSuccess;
		this.resMsg = resMsg;
	}

	public BasicDataResponse(List<T> resDatas, boolean isSuccess) {
		super();
		this.resDatas = resDatas;
		this.isSuccess = isSuccess;
	}

	public BasicDataResponse(List<T> resDatas) {
		super();
		this.resDatas = resDatas;
	}

	@JsonIgnore
	private List<T> resDatas;
	@JsonIgnore
	private boolean isSuccess;
	@JsonIgnore
	private String resMsg;
	
	@JsonProperty("ResDatas")
	public List<T> getResDatas(){
		return this.resDatas;
	}
	@JsonProperty("ResDatas")
	public void setResDatas(List<T> resDatas){
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
