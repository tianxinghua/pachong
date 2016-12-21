package com.shangpin.ephub.product.business.ui.hubCrud.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HubQuryDto {

	private Integer pageIndex;
    private Integer pageSize;
	private String spuModel;
	private String brandNo;
	private String categoryNo;
	private String startUpdateTime;
	private String endUpdateTime;
}
