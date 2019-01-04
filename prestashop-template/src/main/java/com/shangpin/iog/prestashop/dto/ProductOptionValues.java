package com.shangpin.iog.prestashop.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="product_option_values")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOptionValues {
	@javax.xml.bind.annotation.XmlElement(name="product_option_value")
	private List<ProductOptionValue> productOptionValue ;

}
