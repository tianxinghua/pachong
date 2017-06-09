package com.shangpin.ephub.data.mysql.slot.dic.category.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplateCriteria;

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
public class HubDicCategoryMeasureTemplateCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6065866159180047611L;

	private HubDicCategoryMeasureTemplateCriteria criteria;
	
	private RowBounds rowBounds;
}
