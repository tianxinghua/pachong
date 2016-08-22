package com.shangpin.iog.monnierfreres.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class Orders {

	//@javax.xml.bind.annotation.XmlElement(name="order")
	private List<Order> order;
}
