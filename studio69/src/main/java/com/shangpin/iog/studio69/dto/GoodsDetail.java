package com.shangpin.iog.studio69.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GoodsDetail")
@XmlAccessorType(XmlAccessType.NONE)
public class GoodsDetail {
	@XmlElement(name="Good")
	private List<GoodDetail> goodDetials;

	public List<GoodDetail> getGoodDetials() {
		return goodDetials;
	}

	public void setGoodDetials(List<GoodDetail> goodDetials) {
		this.goodDetials = goodDetials;
	}
	
}
