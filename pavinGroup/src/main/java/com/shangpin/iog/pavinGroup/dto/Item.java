package com.shangpin.iog.pavinGroup.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	private String title;
	private String link;
	private String ProductColor;
	private String description;
	private String ProductSize;
	private String material;
	private String group_description;
	private String gender;
	private String SupplierSkuNo;
	private String brand;
	private String price;
	private String images;
	private String pubDate;
	private String stock;
}
