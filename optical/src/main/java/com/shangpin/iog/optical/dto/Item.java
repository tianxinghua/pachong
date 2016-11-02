package com.shangpin.iog.optical.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="brandProduct")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	private String Informazioni;
	private String CategoryName;
	private String BrandName;
	private String ProductModel;
	private String skuNo ;
	private String Gender;
	private String ProductName;
	private String ProductColor;
	private String ProductSize;
	private String material;
	private String ProductOrigin;
	private String productUrl1;
	private String productUrl2;
	private String productUrl3;
	private String ProductDescription;
	private String Stock;
	private String markerPrice;
	private String supplierPrice;
	private String Currency;
	private String season;
}
