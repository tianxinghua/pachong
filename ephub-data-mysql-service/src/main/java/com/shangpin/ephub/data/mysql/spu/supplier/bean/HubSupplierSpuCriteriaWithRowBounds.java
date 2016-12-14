package com.shangpin.ephub.data.mysql.spu.supplier.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;

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
public class HubSupplierSpuCriteriaWithRowBounds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7953439026087059715L;

	private HubSupplierSpuCriteria criteria;
	
	private RowBounds rowBounds;
}
