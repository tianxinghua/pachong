package com.shangpin.ephub.data.mysql.dictionary.gender.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDic;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDicCriteria;

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
public class HubGenderDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383984515755423137L;
	
	private HubGenderDic hubGenderDic;
	
	private HubGenderDicCriteria criteria;

}
