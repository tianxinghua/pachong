package com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@XmlRootElement(name="prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
public class Prestashop {
		@javax.xml.bind.annotation.XmlElement(name="customer")
		private Customer customer;
		@javax.xml.bind.annotation.XmlElement(name="address")
		private Address address;
		@javax.xml.bind.annotation.XmlElement(name="cart")
		private Cart cart;
		@javax.xml.bind.annotation.XmlElement(name="order")
		private Order order;
		@javax.xml.bind.annotation.XmlElement(name="order_history")
		private OrderHistory orderHistory;
}
