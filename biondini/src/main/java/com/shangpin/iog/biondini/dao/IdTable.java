package com.shangpin.iog.biondini.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="IdTable")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdTable {
	private String NomTable;
	private String NumTable;
	private String Position;
	private String Longueur;
	@javax.xml.bind.annotation.XmlElement(name="Description")
	private Description description;
	
	
}
