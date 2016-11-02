package com.shangpin.iog.monnierfreres.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {   
        "orderId",   
        "itemId",   
        "size",   
        "quantity",   
        "date", 
        "price",
        "status"
})  
public class Order {

	//@javax.xml.bind.annotation.XmlElement(name="orderId")
	private String orderId;
	//@javax.xml.bind.annotation.XmlElement(name="itemId")
	private String itemId;
	//@javax.xml.bind.annotation.XmlElement(name="size")
	private String size;
	//@javax.xml.bind.annotation.XmlElement(name="quantity")
	private String quantity;
	//@javax.xml.bind.annotation.XmlElement(name="date")
	private String date;
	//@javax.xml.bind.annotation.XmlElement(name="price")
	private String price;
	//@javax.xml.bind.annotation.XmlElement(name="status")
	private String status;
}
