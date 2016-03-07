package com.shangpin.iog.biondini.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Description")
@XmlAccessorType(XmlAccessType.FIELD)
public class Description {
	@javax.xml.bind.annotation.XmlElement(name="TableMdle")
	private List<TableMdle> tableMdle;
	
	@javax.xml.bind.annotation.XmlElement(name="TableArti")
	private List<TableArti> tableArti;
}
