package com.shangpin.ephub.client.product.business.hubpending.spu.dto;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HubSpuPendingCheckProperty {
	
	private boolean hubSpuModel;
	private boolean hubBrand;
	private boolean hubCategory;
	private boolean hubColor;
	private boolean hubGender;
	private boolean hubMaterial;
	private boolean hubSeason;
	private boolean hubOrigin;
	/**
	 * 待检验的数据
	 */
	private HubSpuPendingDto dto;

}
