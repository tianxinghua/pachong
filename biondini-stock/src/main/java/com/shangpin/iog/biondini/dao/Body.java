package com.shangpin.iog.biondini.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Body")
@XmlAccessorType(XmlAccessType.FIELD)
public class Body {
	@javax.xml.bind.annotation.XmlElement(name="return")
	private Return returns;

}
