package com.shangpin.iog.prestashop.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="products")
@XmlAccessorType(XmlAccessType.FIELD)
public class Products {
	@javax.xml.bind.annotation.XmlElement(name="product")
	private List<Product> product;

}
