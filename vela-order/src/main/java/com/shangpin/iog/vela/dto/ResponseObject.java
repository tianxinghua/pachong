package com.shangpin.iog.vela.dto;

public class ResponseObject {
	
	private Integer Id_b2b_order;
	private String Order_No;
	private String Purchase_No;
	private String Status;
	private String Message;
	
	private String Date_Order;
	private String logistics_company;
	private String Trk_Number;
	
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
	
	public String getDate_Order() {
		return Date_Order;
	}
	public void setDate_Order(String date_Order) {
		Date_Order = date_Order;
	}

	public String getLogistics_company() {
		return logistics_company;
	}

	public void setLogistics_company(String logistics_company) {
		this.logistics_company = logistics_company;
	}

	public String getTrk_Number() {
		return Trk_Number;
	}
	public void setTrk_Number(String trk_Number) {
		Trk_Number = trk_Number;
	}
	
	
}
