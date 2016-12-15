package com.shangpin.ephub.data.mysql.dictionary.categroy.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDicCriteria;

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
public class HubSupplierCategroyDicCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1926357322561237043L;

	private HubSupplierCategroyDicCriteria criteria;
	
	private RowBounds rowBounds;
}
