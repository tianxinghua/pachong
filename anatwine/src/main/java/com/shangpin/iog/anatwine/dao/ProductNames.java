package com.shangpin.iog.anatwine.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.tracing.dtrace.Attributes;
import com.sun.tracing.dtrace.NameAttributes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="productNames")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductNames {
	
	@XmlElement(name="productName")
	private List<ProductName> proName;

}
