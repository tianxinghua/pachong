package com.shangpin.iog.railSoAtelier.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="attributes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Attributes {
	
	 @XmlElement(name="item")
	    private List<Attribute> attributes;

	
}
