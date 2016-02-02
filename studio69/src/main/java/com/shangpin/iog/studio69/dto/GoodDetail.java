package com.shangpin.iog.studio69.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Good")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodDetail {
	private String ID;
	private String Color;
	private String Composition;
	@XmlElement(name="Stock")
	private Stock stock;
	private String MadeIn;
	@XmlElement(name="Pictures")
	private Pictures pictures;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public String getComposition() {
		return Composition;
	}
	public void setComposition(String composition) {
		Composition = composition;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public Pictures getPictures() {
		return pictures;
	}
	public void setPictures(Pictures pictures) {
		this.pictures = pictures;
	}
	public String getMadeIn() {
		return MadeIn;
	}
	public void setMadeIn(String madeIn) {
		MadeIn = madeIn;
	}
	
}
