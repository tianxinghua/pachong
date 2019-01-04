package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="id_attribute_group")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdAttributeGroup {
	@XmlAttribute(name="href",namespace="http://www.w3.org/1999/xlink")
	private String href;
	@XmlValue
	private String text;

}
