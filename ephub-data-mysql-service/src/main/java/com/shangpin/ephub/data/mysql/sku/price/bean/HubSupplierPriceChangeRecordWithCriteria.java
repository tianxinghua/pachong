package com.shangpin.ephub.data.mysql.sku.price.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecord;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecordCriteria;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierPriceChangeRecordWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8549460796668466239L;

	private HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord;
	
	private HubSupplierPriceChangeRecordCriteria criteria;
}
