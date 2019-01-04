package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="associations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Associations {
	@javax.xml.bind.annotation.XmlElement(name="categories")
	private Categories categories;
	@javax.xml.bind.annotation.XmlElement(name="images")
	private Images images;
	@javax.xml.bind.annotation.XmlElement(name="combinations")
	private Combinations combinations;
	
	@javax.xml.bind.annotation.XmlElement(name="product_option_values")
	private ProductOptionValues productOptionValues ;
	

	@javax.xml.bind.annotation.XmlElement(name="stock_availables")
	private StockAvailables  stockAvailables ;
}
