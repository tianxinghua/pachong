package com.shangpin.iog.railSoAtelier.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="descriptions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Description {
	
	private String name;
	private String link_rewrite;
	private String description_short;

}
