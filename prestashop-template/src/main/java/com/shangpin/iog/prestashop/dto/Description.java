package com.shangpin.iog.prestashop.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="description")
@XmlAccessorType(XmlAccessType.FIELD)
public class Description {
	@javax.xml.bind.annotation.XmlElement(name="language")
	private List<Language> Language;
}
