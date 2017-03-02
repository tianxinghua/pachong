package com.shangpin.iog.vela.stock.dto;

public class Stock {
	private String Barcode;
	private String Qty;

	public void setBarcode(String Barcode) {
		this.Barcode = Barcode;
	}

	public void setQty(String Qty) {
		this.Qty = Qty;
	}

	public String getBarcode() {
		return this.Barcode;
	}

	public String getQty() {
		return this.Qty;
	}
}