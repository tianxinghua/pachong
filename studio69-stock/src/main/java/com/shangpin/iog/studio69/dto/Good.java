package com.shangpin.iog.studio69.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Good")
@XmlAccessorType(XmlAccessType.FIELD)
public class Good {
	private String ID;
	@XmlElement(name="Stocks")
	private Stocks stocks;
	public String getID() {
		return ID; 
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Stocks getStocks() {
		return stocks;
	}
	public void setStocks(Stocks stocks) {
		this.stocks = stocks;
	}

}
