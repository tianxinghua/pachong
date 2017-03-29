package com.shangpin.ephub.data.mysql.sku.price.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecordCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierPriceChangeRecordWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2529430660239869178L;

	private HubSupplierPriceChangeRecordCriteria criteria;
	
	private RowBounds rowBounds;
}
