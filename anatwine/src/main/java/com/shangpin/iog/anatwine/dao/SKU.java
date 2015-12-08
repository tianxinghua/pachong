package com.shangpin.iog.anatwine.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@XmlRootElement(name="SKU")
@XmlAccessorType(XmlAccessType.FIELD)
public class SKU {
	
	private String brandSKUId;
	private String ean;
	private String anatwineSKUId;
	private String sizeGridValue;
	private String primaryColourCode;
	private String secondaryColourCode;
	private String tertiaryColourCode;
	private String [] brandColourName;
	@XmlElement(name="genders")
	private Genders genders;
}
