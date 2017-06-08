package com.shangpin.ephub.data.mysql.slot.dic.category.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplate;
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
public class HubDicCategoryMeasureTemplateWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubDicCategoryMeasureTemplate hubDicCategoryMeasureTemplate;
	
	private HubDicCategoryMeasureTemplateCriteria criteria;
}
