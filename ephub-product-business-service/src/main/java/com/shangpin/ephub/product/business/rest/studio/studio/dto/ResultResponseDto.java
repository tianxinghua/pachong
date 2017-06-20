package com.shangpin.ephub.product.business.rest.studio.studio.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

/**
 * Created by wangchao on 2017/06/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ResultResponseDto<T> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7377947555463865071L;
	private List<T> resultObjList;
    private boolean success;
    private String message;
    private boolean isHappenEx;
    private String exMessage;
    private int effectRows;

    @JsonProperty("Success")
    public boolean getSuccess() {
        return success;
    }
    @JsonProperty("Success")
    public void setSuccess(boolean success) {
    	this.success = success;
    }
    @JsonProperty("ResultObjList")
    public List<T> getResultObjList() {
        return resultObjList;
    }

    @JsonProperty("ResultObjList")
    public void setResultObjList(List<T> resultObjList) {
        this.resultObjList = resultObjList;
    }
    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("Message")
    public void setMessage(String message) {
    	this.message = message;
    }
    @JsonProperty("ExMessage")
    public String getExMessage() {
        return exMessage;
    }
    @JsonProperty("ExMessage")
    public void setExMessage(String exMessage) {
    	this.exMessage = exMessage;
    }
    @JsonProperty("IsHappenEx")
    public boolean getIsHappenEx() {
        return isHappenEx;
    }
    @JsonProperty("IsHappenEx")
    public void setIsHappenEx(boolean isHappenEx) {
    	this.isHappenEx = isHappenEx;
    }
    @JsonProperty("EffectRows")
    public int getEffectRows() {
        return effectRows;
    }
    @JsonProperty("EffectRows")
    public void setEffectRows(int effectRows) {
    	this.effectRows = effectRows;
    }
}
