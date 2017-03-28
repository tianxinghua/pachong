package com.shangpin.ephub.product.business.ui.pending.vo;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingOriginVo {
	
	private PendingProductDto pendingProduct;
	private SupplierProductVo supplierProduct;
	/**
	 * 标准货号
	 */
	private HubBrandModelRuleDto brandModelRuleDto;

}
