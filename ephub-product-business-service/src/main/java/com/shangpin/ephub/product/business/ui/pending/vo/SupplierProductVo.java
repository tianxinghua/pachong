package com.shangpin.ephub.product.business.ui.pending.vo;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierProductVo extends HubSupplierSpuDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5634253012222804461L;
	private List<HubSupplierSkuDto> supplierSku;
	private String updateTimeStr;
	

}
