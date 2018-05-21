package com.shangpin.ephub.data.schedule.service.dto;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2017/12/29.
 */
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

