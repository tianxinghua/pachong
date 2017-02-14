package com.shangpin.iog.studio69.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name="Category")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class SecondCategory {

	String ID;
	String Name;
	String ParentID;
	String ParentName;
	String GoodsType;
}
