package com.shangpin.iog.anatwine.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="productShortDescriptions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Descriptions {
	
	@XmlElement(name="productShortDescription")
	private List<Description> description;

}
