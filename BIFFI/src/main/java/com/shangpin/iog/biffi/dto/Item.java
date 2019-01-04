package com.shangpin.iog.biffi.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement
public class Item {

	String id;
	String name;
	Photos photos; 
	String brand;
	String category;
	String gender;
	String prezzo;
	String description;
	String fit;
	String composition;
	String madein;
	Details details;
}
