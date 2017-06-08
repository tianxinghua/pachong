package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.studio.data.mysql.po.dic.StudioDicCategory;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCategoryCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioDicCategoryWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioDicCategory studioDicCategory;
	
	private StudioDicCategoryCriteria criteria;
}
