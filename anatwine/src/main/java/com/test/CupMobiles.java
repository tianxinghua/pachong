package com.test;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * xml对应的java模型
 * @author wangyfc
 *
 */
@XmlRootElement
public class CupMobiles {
	private CupMobile cupMobile;
	private String msgResponseCode;
	public CupMobile getCupMobile() {
		return cupMobile;
	}
	public void setCupMobile(CupMobile cupMobile) {
		this.cupMobile = cupMobile;
	}
	public String getMsgResponseCode() {
		return msgResponseCode;
	}
	public void setMsgResponseCode(String msgResponseCode) {
		this.msgResponseCode = msgResponseCode;
	}
	
}