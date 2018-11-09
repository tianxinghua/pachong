package com.shangpin.ep.order.module.order.bean;

import java.io.Serializable;
import java.util.Date;

public class TokenDTO implements Serializable{

	private static final long serialVersionUID = 4078982631318898469L;
	
	private String id;
	private String Token;
	private String accessToken;
	private Date createDate = new Date();
	private String expireTime;
	private String supplierId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String Token) {
		this.Token = Token;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}


}
