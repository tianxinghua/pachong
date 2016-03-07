package com.shangpin.iog.biondini.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Table_Modele")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableModele {
	@javax.xml.bind.annotation.XmlElement(name="IdTable")
	private List<IdTable> idTable;
}
