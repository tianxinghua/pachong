package com.shangpin.iog.studio69.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BrandList")
@XmlAccessorType(XmlAccessType.NONE)
public class BrandList {
	@XmlElement(name="Brand")
	private List<Brand> brand;

	public List<Brand> getBrand() {
		return brand;
	}

	public void setBrand(List<Brand> brand) {
		this.brand = brand;
	}
		
}
