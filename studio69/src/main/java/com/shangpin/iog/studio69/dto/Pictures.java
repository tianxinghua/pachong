package com.shangpin.iog.studio69.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pictures {
	@XmlElement(name="Picture")
	private List<Picture> picturelist;

	public List<Picture> getPicturelist() {
		return picturelist;
	}

	public void setPicturelist(List<Picture> picturelist) {
		this.picturelist = picturelist;
	}
	
}
