package com.shangpin.iog.syn.sku.supplier.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.iog.syn.sku.supplier.po.HubSupplierSku;
import com.shangpin.iog.syn.sku.supplier.po.HubSupplierSkuCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierSkuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8549460796668466239L;

	private HubSupplierSku hubSupplierSku;
	
	private HubSupplierSkuCriteria criteria;
}
