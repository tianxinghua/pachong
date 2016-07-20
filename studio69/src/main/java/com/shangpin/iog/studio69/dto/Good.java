package com.shangpin.iog.studio69.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name="Good")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Good {
	private String ID;
	private String Model;
	private String Variante;
	private String Season;
	private String Collection;
	private String SupplierID;
	private String BrandID;
	private String ParentCategoryID;
	private String CategoryID;
//	private String CID;
//	private String GoodsCode;
	private String TypeID;
	private String Code;
	private String GoodsName;
	private String Price;
	private String Stock;
	private String MainPicNo;
	private String CreatedTime;
	private String ModifyTime;
	
}
