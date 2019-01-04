package com.shangpin.iog.anatwine.dao;

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
@XmlRootElement(name="brandProducts",namespace="http://anatwine.com/redacted")
@XmlAccessorType(XmlAccessType.FIELD)
public class BrandProducts {
	
	
	@XmlElement(name="header")
	private Header header;
	@XmlElement(name="footer")
	private Footer footer;
	@XmlElement(name="brandProduct")
	private List<BrandProduct> brandProduct;

}
