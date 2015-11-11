package com.shangpin.iog.spinnaker.dto;

public class ResponseObject {
	
	private Integer Id_b2b_order;
	private String Order_No;
	private String Purchase_No;
	private String Status;
	private String Message;
	
	public Integer getId_b2b_order() {
		return Id_b2b_order;
	}
	public void setId_b2b_order(Integer id_b2b_order) {
		Id_b2b_order = id_b2b_order;
	}
	public String getOrder_No() {
		return Order_No;
	}
	public void setOrder_No(String order_No) {
		Order_No = order_No;
	}
	public String getPurchase_No() {
		return Purchase_No;
	}
	public void setPurchase_No(String purchase_No) {
		Purchase_No = purchase_No;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	
	
}
