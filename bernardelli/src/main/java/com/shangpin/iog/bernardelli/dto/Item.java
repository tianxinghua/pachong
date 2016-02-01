package com.shangpin.iog.bernardelli.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Item {
	private String item_id;
	private String barcode;
	private String stock;
	private String item_size;
}
