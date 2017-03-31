package com.shangpin.ephub.client.product.business.price.dto;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceDto {

	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * 
	 */
	private HubSupplierSpuDto hubSpu;
	/**
	 * 
	 */
	private List<HubSupplierSkuDto> hubSkus;
}
