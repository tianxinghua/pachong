package com.shangpin.iog.dto;

import java.io.Serializable;
import java.util.Date;

public class StockUpdateDTO implements Serializable{

	private static final long serialVersionUID = 4078982631318898469L;
	
	private String supplierId;
	private Date updateTime = new Date();
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
