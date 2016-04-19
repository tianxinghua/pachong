package com.shangpin.iog.tessabit.stock.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="DocumentElement")
@XmlAccessorType(XmlAccessType.FIELD)
public class Picture {
	@XmlAttribute(name="Date")
	private String Date;
	@XmlElement(name="Riferimenti")
	private List<Riferimenti> list;

}
