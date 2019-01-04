package com.shangpin.iog.prestashop.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {
//	  @XmlAttribute(name="id")
	  private String id;
	  private String manufacturer_name;
	  
	  @XmlAttribute(name="href",namespace="http://www.w3.org/1999/xlink")
	  private String href;
	  @javax.xml.bind.annotation.XmlElement(name="description")
	  private Description description;
	  @javax.xml.bind.annotation.XmlElement(name="description_short")
	  private DescriptionShort description_short;
	  @javax.xml.bind.annotation.XmlElement(name="associations")
	  private Associations associations;
	  @javax.xml.bind.annotation.XmlElement(name="name")
	  private Name name;
}
