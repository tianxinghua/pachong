package com.shangpin.ephub.data.mysql.supplier.config.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class HubSupplierConfigCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612231902151503140L;

	private HubSupplierConfigCriteria criteria;
	
	private RowBounds rowBounds;
}
