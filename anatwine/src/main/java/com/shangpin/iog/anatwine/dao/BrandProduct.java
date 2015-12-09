package com.shangpin.iog.anatwine.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@XmlRootElement(name="brandProduct")
@XmlAccessorType(XmlAccessType.FIELD)
public class BrandProduct {
	@XmlElement(name="product")
	private List<Product> ListProduct;
	@XmlAttribute(name="brandCode")
	private String brandCode;

}
