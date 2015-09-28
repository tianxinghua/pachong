package com.shangpin.iog.fashionesta.dto;

public class Item {
	//不要忘了存币种
	private String itemCode;//sku->skuid
	private String size;//size->sku:size
	private String stock;//qty->stock
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	
}
