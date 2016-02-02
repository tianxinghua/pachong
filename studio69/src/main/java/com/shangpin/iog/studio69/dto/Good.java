package com.shangpin.iog.studio69.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Good")
@XmlAccessorType(XmlAccessType.FIELD)
public class Good {
	private String ID;
	private String Season;
	private String BrandID;
	private String CID;
	private String GoodsCode;
	private String TypeID;
	private String GoodsName;
	private String Price;
	private String Stock;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSeason() {
		return Season;
	}
	public void setSeason(String season) {
		Season = season;
	}
	public String getBrandID() {
		return BrandID;
	}
	public void setBrandID(String brandID) {
		BrandID = brandID;
	}
	public String getCID() {
		return CID;
	}
	public void setCID(String cID) {
		CID = cID;
	}
	public String getGoodsCode() {
		return GoodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}
	public String getTypeID() {
		return TypeID;
	}
	public void setTypeID(String typeID) {
		TypeID = typeID;
	}
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getStock() {
		return Stock;
	}
	public void setStock(String stock) {
		Stock = stock;
	}
}
