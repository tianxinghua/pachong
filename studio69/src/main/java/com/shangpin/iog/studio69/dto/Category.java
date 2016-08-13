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
public class Category {
	private String CID;
	private String Name;
	private String CategoryID;
	private String TypeID;
	private String SKUCount;
	public String getCID() {
		return CID;
	}
	public void setCID(String cID) {
		CID = cID;
	}
	public String getSKUCount() {
		return SKUCount;
	}
	public void setSKUCount(String sKUCount) {
		SKUCount = sKUCount;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getTypeID() {
		return TypeID;
	}
	public void setTypeID(String typeID) {
		TypeID = typeID;
	}
	
}
