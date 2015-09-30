package com.shangpin.iog.itemInfo.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DocumentElement")
@XmlAccessorType(XmlAccessType.NONE)
public class SkuStoks {

	@XmlElement(name="SkuStok")
	private List<SkuStok> skuStoks;

	public List<SkuStok> getSkuStoks() {
		return skuStoks;
	}

	public void setSkuStoks(List<SkuStok> skuStoks) {
		this.skuStoks = skuStoks;
	}
	
}
