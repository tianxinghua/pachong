package com.shangpin.ephub.product.business.rest.studio.studio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class StudioRequestDto {

	@JsonIgnore
	public String from;
	@JsonIgnore
    public String to;
	@JsonIgnore
    public String calendarNo;
    
    @JsonProperty("From")
	public String getFrom() {
		return from;
	}
    @JsonProperty("BrandNo")
	public void setFrom(String from) {
		this.from = from;
	}
    @JsonProperty("To")
	public String getTo() {
		return to;
	}
    @JsonProperty("To")
	public void setTo(String to) {
		this.to = to;
	}
    @JsonProperty("CategoryNo")
	public String getCategoryNo() {
		return calendarNo;
	}
    @JsonProperty("CategoryNo")
	public void setCategoryNo(String calendarNo) {
		this.calendarNo = calendarNo;
	}
    
    
}
