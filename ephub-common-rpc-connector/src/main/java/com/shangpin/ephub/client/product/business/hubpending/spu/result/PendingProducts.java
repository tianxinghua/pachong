package com.shangpin.ephub.client.product.business.hubpending.spu.result;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingProducts {

	private List<PendingProductDto> produts;
	private int total;
	private String createUser;
	private String supplierName;
	private String hubBrandName;
	private String hubCategoryName;
	private List<HubSkuPendingDto> hubSkus;
	private String spPicUrl;
	private List<String> picUrls;
	
	private String supplierCategoryname;
}
