package com.shangpin.iog.optical.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Worksheet")
@XmlAccessorType(XmlAccessType.FIELD)
public class Worksheet {

	@XmlElement(name="Table")
	private Table table;
	
	@XmlElement(name="Names")
	private Names names;
}
