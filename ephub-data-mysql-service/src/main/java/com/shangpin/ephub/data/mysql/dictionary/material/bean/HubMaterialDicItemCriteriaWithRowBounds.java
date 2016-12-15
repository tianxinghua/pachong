package com.shangpin.ephub.data.mysql.dictionary.material.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItemCriteria;

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
public class HubMaterialDicItemCriteriaWithRowBounds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4526866331183281478L;

	private HubMaterialDicItemCriteria criteria;
	
	private RowBounds rowBounds;
}
