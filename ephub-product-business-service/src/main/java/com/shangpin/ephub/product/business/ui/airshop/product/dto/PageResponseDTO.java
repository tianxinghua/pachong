package com.shangpin.ephub.product.business.ui.airshop.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageResponseDTO {
	
	private List<ProductDTO> list;
	private int count;

}
