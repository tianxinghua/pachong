package com.shangpin.iog.anatwine.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.tracing.dtrace.Attributes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {
	
	private String anatwineProductId;
	private String brandProductId;
	
	@XmlElement(name="productNames")
	private ProductNames productNames;
	
	@XmlElement(name="categoryCodes")
	private CategoryCodes categoryCodes;
	
	@XmlElement(name="productShortDescriptions")
	private Descriptions descriptions;

	private String season;
	private String sizeGridCode;
	
	@XmlElement(name="SKUs")
	private SKUs SKUs;
}
