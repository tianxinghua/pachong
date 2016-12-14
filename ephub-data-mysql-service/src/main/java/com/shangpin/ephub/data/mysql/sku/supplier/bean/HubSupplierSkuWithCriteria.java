package com.shangpin.ephub.data.mysql.sku.supplier.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSkuCriteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
