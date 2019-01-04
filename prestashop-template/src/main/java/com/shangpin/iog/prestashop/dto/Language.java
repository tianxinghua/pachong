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
@XmlRootElement(name="language")
@XmlAccessorType(XmlAccessType.FIELD)
public class Language {
	//id=1代表英文
	@XmlAttribute(name="id")
	private String id;
	@XmlValue
	private String text;
}
