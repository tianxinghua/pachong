package com.shangpin.iog.syn.sku.supplier.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.iog.syn.sku.supplier.po.HubSupplierSkuCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierSkuCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2529430660239869178L;

	private HubSupplierSkuCriteria criteria;
	
	private RowBounds rowBounds;
}
