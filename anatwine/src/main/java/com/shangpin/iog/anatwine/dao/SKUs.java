package com.shangpin.iog.anatwine.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@XmlRootElement(name="SKUs")
@XmlAccessorType(XmlAccessType.FIELD)
public class SKUs {


	@XmlElement(name="SKU")
	private List<SKU> listSKU;
}
