package com.shangpin.iog.biondini.dao;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@XmlRootElement(name="Envelope",namespace="http://schemas.xmlsoap.org/soap/envelope/")
@XmlAccessorType(XmlAccessType.FIELD)
public class SOAP {
	
	@javax.xml.bind.annotation.XmlElement(name="Body",namespace="http://schemas.xmlsoap.org/soap/envelope/")
	private Body body;
}
