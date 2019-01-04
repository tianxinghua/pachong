package com.shangpin.iog.prestashop.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="stock_availables")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockAvailables {
	@javax.xml.bind.annotation.XmlElement(name="stock_available")
	private List<StockAvailable> stock_available ;

}
