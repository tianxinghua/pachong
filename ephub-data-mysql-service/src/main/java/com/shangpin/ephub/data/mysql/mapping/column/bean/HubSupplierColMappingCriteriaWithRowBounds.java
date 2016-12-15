package com.shangpin.ephub.data.mysql.mapping.column.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMappingCriteria;

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
public class HubSupplierColMappingCriteriaWithRowBounds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8279189311561500197L;

	private HubSupplierColMappingCriteria criteria;
	
	private RowBounds rowBounds;
}
