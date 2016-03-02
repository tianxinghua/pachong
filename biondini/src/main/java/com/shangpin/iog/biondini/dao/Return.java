package com.shangpin.iog.biondini.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="return")
@XmlAccessorType(XmlAccessType.FIELD)
public class Return {
	@javax.xml.bind.annotation.XmlElement(name="Resultat")
	private Resultat result;
}
