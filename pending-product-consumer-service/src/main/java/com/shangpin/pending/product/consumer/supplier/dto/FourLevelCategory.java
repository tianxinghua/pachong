package com.shangpin.pending.product.consumer.supplier.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FourLevelCategory {

	/**
	 * 一级品类编号
	 */
	@JsonIgnore
	private String firstNo;
	/**
	 * 一级品类名称
	 */
	@JsonIgnore
	private String firstName ;
	/**
	 * 二级品类编号
	 */
	@JsonIgnore
	private String secondNo;
	/**
	 * 二级品类名称
	 */
	@JsonIgnore
	private String secondName;
	/**
	 * 三级品类编号
	 */
	@JsonIgnore
	private String thirdNo;
	/**
	 * 三级品类名称
	 */
	@JsonIgnore
	private String thirdName;
	/**
	 * 四级品类编号
	 */
	@JsonIgnore
	private String fourthNo;
	/**
	 * 四级品类名称
	 */
	@JsonIgnore
	private String fourthName;
	@JsonIgnore
	private String sizeTmpNo;
	
	@JsonProperty("FirstNo")
	public String getFirstNo() {
		return firstNo;
	}
	@JsonProperty("FirstNo")
	public void setFirstNo(String firstNo) {
		this.firstNo = firstNo;
	}
	@JsonProperty("FirstName")
	public String getFirstName() {
		return firstName;
	}
	@JsonProperty("FirstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@JsonProperty("SecondNo")
	public String getSecondNo() {
		return secondNo;
	}
	@JsonProperty("SecondNo")
	public void setSecondNo(String secondNo) {
		this.secondNo = secondNo;
	}
	@JsonProperty("SecondName")
	public String getSecondName() {
		return secondName;
	}
	@JsonProperty("SecondName")
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	@JsonProperty("ThirdNo")
	public String getThirdNo() {
		return thirdNo;
	}
	@JsonProperty("ThirdNo")
	public void setThirdNo(String thirdNo) {
		this.thirdNo = thirdNo;
	}
	@JsonProperty("ThirdName")
	public String getThirdName() {
		return thirdName;
	}
	@JsonProperty("ThirdName")
	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}
	@JsonProperty("FourthNo")
	public String getFourthNo() {
		return fourthNo;
	}
	@JsonProperty("FourthNo")
	public void setFourthNo(String fourthNo) {
		this.fourthNo = fourthNo;
	}
	@JsonProperty("FourthName")
	public String getFourthName() {
		return fourthName;
	}
	@JsonProperty("FourthName")
	public void setFourthName(String fourthName) {
		this.fourthName = fourthName;
	}
	@JsonProperty("SizeTmpNo")
	public String getSizeTmpNo() {
		return sizeTmpNo;
	}
	@JsonProperty("SizeTmpNo")
	public void setSizeTmpNo(String sizeTmpNo) {
		this.sizeTmpNo = sizeTmpNo;
	}	
	
}
