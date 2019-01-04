package com.shangpin.iog.studio69.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GoodsStock")
@XmlAccessorType(XmlAccessType.NONE)
public class GoodsStock {
	@XmlElement(name="Good")
	private List<Good> good;

	public List<Good> getGood() {
		return good;
	}

	public void setGood(List<Good> good) {
		this.good = good;
	}
}
