package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

public class ResponseObject {
	
	private String return_code;
	private String error_code;
	private String error_msg;
	private Object response;
	private String reservation_id;
	private String order_id;
	//{"access_token":"e33f823d4ce7e66cbd48dc965d0b702071cb5502","expires_in":86400,"token_type":"bearer","scope":"","refresh_token":"e469ca9171c1a1e9851153b635ef2ddaacc39d4f"}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getReservation_id() {
		return reservation_id;
	}
	public void setReservation_id(String reservation_id) {
		this.reservation_id = reservation_id;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	
	
}