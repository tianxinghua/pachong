package com.shangpin.iog.reebonz.dto;

public class RequestObject {

	private String event_id;
	private String sku;
	private String option_code;
	private String qty;

	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getOption_code() {
		return option_code;
	}
	public void setOption_code(String option_code) {
		this.option_code = option_code;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
}