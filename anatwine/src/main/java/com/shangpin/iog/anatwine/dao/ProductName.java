package com.shangpin.iog.anatwine.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="productName")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductName {
	
	@XmlAttribute(name="countryCode")
	private String countryCode;
	@XmlValue
	private String productName;
}
