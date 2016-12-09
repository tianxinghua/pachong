package com.shangpin.ep.order.module.orderapiservice.impl.dto.spinnaker;

public class RequestObject {

	private String event_id;
	private String sku;
	private String option_code;
	private String qty;
	private String payment_price;

	public String getPayment_price() {
		return payment_price;
	}
	public void setPayment_price(String payment_price) {
		this.payment_price = payment_price;
	}
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

	@Override
	public String toString() {
		return "RequestObject{" +
				"event_id='" + event_id + '\'' +
				", sku='" + sku + '\'' +
				", option_code='" + option_code + '\'' +
				", qty='" + qty + '\'' +
				", payment_price='" + payment_price + '\'' +
				'}';
	}
}
