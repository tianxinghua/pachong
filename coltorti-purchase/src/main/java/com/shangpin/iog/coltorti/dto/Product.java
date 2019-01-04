package com.shangpin.iog.coltorti.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Product {
	private String id;
	private String deposit_id;
	private Map quantity;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeposit_id() {
		return deposit_id;
	}
	public void setDeposit_id(String deposit_id) {
		this.deposit_id = deposit_id;
	}
	public Map getQuantity() {
		return quantity;
	}
	public void setQuantity(Map quantity) {
		this.quantity = quantity;
	}
	public Product(String id, String deposit_id, Map quantity) {
		super();
		this.id = id;
		this.deposit_id = deposit_id;
		this.quantity = quantity;
	}
	public Product() {
		super();
	}
	
}
