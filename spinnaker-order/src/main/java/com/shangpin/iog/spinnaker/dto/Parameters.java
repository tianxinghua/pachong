package com.shangpin.iog.spinnaker.dto;

public class Parameters {
	private String DBContext;
	private String purchase_no;
	private String order_no;
	private String barcode;
	private String ordQty;
	
	public String getDBContext() {
		return DBContext;
	}
	public void setDBContext(String dBContext) {
		DBContext = dBContext;
	}
	public String getPurchase_no() {
		return purchase_no;
	}
	public void setPurchase_no(String purchase_no) {
		this.purchase_no = purchase_no;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(String ordQty) {
		this.ordQty = ordQty;
	}

}
