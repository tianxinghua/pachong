package com.shangpin.iog.biondini.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="TableMdle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableMdle {
	private String Code;
	private String Libelle;
}
