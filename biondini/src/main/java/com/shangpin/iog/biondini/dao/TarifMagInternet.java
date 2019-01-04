package com.shangpin.iog.biondini.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="TarifMagInternet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TarifMagInternet {
	
	@XmlElement(name="QtTaille")
	private List<QtTaille> list;

}
