package com.shangpin.ephub.client.product.business.hubpending.spu.result;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PendingProductDto extends HubSpuPendingDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -656519843276622266L;

	private String updateTimeStr;
	private String supplierName;
	private String hubBrandName;
	private String hubCategoryName;
	private List<HubSkuPendingDto> hubSkus;
	private String spPicUrl;
	
}
