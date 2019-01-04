package com.shangpin.iog.railSo.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="default_category")
public class Category {
	
	private String category_default_name;

}
