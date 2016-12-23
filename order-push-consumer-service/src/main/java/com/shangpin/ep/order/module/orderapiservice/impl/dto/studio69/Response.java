package com.shangpin.ep.order.module.orderapiservice.impl.dto.studio69;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

	
	private String Result;
	
	private String Message;
}