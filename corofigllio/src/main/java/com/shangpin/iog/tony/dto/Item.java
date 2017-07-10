package com.shangpin.iog.tony.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Item {
	
	private String item_id;
	private String item_size;
	private String barcode;
	private String color;
	private String stock;
	private String last_modified;
	private String [] pictures;

}
