package com.shangpin.iog.railSoAtelier.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="item")
public class Attribute {
	private String quantity;
	private String attribute_name;
}
