package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="category")
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {
	private String id;
	@XmlAttribute(name="href",namespace="http://www.w3.org/1999/xlink")
	private String href;
	 @javax.xml.bind.annotation.XmlElement(name="name")
	private Name name;
}
