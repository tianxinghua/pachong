package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
/**
 * 此链接获取颜色和尺码信息
 * @author Administrator
 *
 */
@Setter
@Getter
@XmlRootElement(name="product_option_value")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOptionValue {
	@XmlAttribute(name="href",namespace="http://www.w3.org/1999/xlink")
	private String href;
	private String id;
	
	//1:代表尺码信息  2：代表颜色信息
	@javax.xml.bind.annotation.XmlElement(name="id_attribute_group")
	private IdAttributeGroup idAttributeGroup;
	@javax.xml.bind.annotation.XmlElement(name="name")
	private Name name;
}
