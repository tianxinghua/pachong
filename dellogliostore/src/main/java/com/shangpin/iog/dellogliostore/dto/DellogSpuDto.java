package com.shangpin.iog.dellogliostore.dto;

import java.util.List;

import com.shangpin.iog.dto.SpuDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DellogSpuDto extends SpuDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465101223592615698L;
	
	private List<String> pictures;
	
	private List<DellogSkuDto> skus;
	
	private String color;
	
	private String productModel;

}
