package com.shangpin.ephub.product.business.rest.studio.studio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;
/**
 * Created by wangchao on 2017/06/19.
 */
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
    @JsonProperty("From")
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
    @JsonProperty("CalendarNo")
	public String getCalendarNo() {
		return calendarNo;
	}
    @JsonProperty("CalendarNo")
	public void setCalendarNo(String calendarNo) {
		this.calendarNo = calendarNo;
	}
    
    
}
