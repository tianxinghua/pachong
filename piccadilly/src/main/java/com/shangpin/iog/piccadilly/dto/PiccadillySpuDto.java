package com.shangpin.iog.piccadilly.dto;

import java.util.List;

import com.shangpin.iog.dto.SpuDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PiccadillySpuDto extends SpuDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465101223592615698L;
	
	private List<String> pictures;
	
	private List<PiccadillySkuDto> skus;
	
	private String color;
	
	private String productModel;

}
