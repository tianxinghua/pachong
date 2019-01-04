package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="stock_available")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockAvailable {

	private String id;
	private String id_product_attribute;
}
