package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
/**
 * 获取sku信息
 * @author Administrator
 *
 */
@Setter
@Getter
@XmlRootElement(name="combination")
@XmlAccessorType(XmlAccessType.FIELD)
public class Combination {

	private String id;
	@XmlAttribute(name="href",namespace="http://www.w3.org/1999/xlink")
	private String href;
	
	private String id_product;
	private String ean13;
	private String quantity;
	private String reference;
	private String price;
	
	private Associations associations;
}
