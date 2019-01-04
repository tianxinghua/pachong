package com.shangpin.iog.biffi.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class Detail {

	String barcode;
	String color;
	String size;
	String qty;
}
