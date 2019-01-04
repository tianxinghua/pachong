package com.shangpin.iog.optical.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Table")
@XmlAccessorType(XmlAccessType.FIELD)
public class Table {
	@XmlElement(name="Row")
	private List<Row> list;
}
