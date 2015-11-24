package com.shangpin.iog.dto;

import java.io.Serializable;
import java.util.Date;

public class StockUpdateDTO implements Serializable{

	private static final long serialVersionUID = 4078982631318898469L;
	
	private String supplierId;
	private String supplierName;
	private Date updateTime ;
	private String dif;
	private String errorNum;
	private String rightNum;
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
	public String getDif() {
		return dif;
	}
	public void setDif(String dif) {
		this.dif = dif;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getErrorNum() {
		return errorNum;
	}
	public void setErrorNum(String errorNum) {
		this.errorNum = errorNum;
	}
	public String getRightNum() {
		return rightNum;
	}
	public void setRightNum(String rightNum) {
		this.rightNum = rightNum;
	}
	
}
