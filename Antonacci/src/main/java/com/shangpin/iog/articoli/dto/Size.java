package com.shangpin.iog.articoli.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="size")
public class Size {

	String size_desc;
	String size_stock;
}
