package com.shangpin.ephub.data.mysql.supplier.config.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfig;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfigCriteria;

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
public class HubSupplierConfigWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6756921226835815577L;

	private HubSupplierConfig hubSupplierConfig;
	
	private HubSupplierConfigCriteria criteria;
}
