package com.shangpin.iog.ostore.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtelierDTO {
	
	private String spuId;
	private String spu;
	private List<String> sku;
	private List<String> image;
	private String price;

}
