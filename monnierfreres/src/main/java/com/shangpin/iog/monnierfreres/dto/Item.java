package com.shangpin.iog.monnierfreres.dto;

public class Item {
	//不要忘了存币种
	private String itemCode;//sku->skuid
	private String size;//size->sku:size
	private String stock;//qty->stock
	private String price;//sku
	private String special_price;//sku
	private String barCode;
	private String currency;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSpecial_price() {
		return special_price;
	}
	public void setSpecial_price(String special_price) {
		this.special_price = special_price;
	}
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
