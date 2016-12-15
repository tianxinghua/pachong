package com.shangpin.ephub.data.mysql.mapping.sku.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMappingCriteria;

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
public class HubSkuSupplierMappingCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4945420864511867151L;

	private HubSkuSupplierMappingCriteria criteria;
	
	private RowBounds rowBounds;
}
