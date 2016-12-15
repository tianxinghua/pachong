package com.shangpin.ephub.data.mysql.dictionary.material.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicCriteria;

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
public class HubMaterialDicCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4424113158499069671L;

	private HubMaterialDicCriteria criteria;
	
	private RowBounds rowBounds;
}
