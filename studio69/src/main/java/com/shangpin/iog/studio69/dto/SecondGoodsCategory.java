package com.shangpin.iog.studio69.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name="GoodsCategory")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class SecondGoodsCategory {

	@XmlElement(name="Category")
	private List<SecondCategory> secondCategory;
}
