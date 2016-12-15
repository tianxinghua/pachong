package com.shangpin.ephub.data.mysql.dictionary.season.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDic;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDicCriteria;

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
public class HubSeasonDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7981932787818888641L;

	private HubSeasonDic hubSeasonDic;
	
	private HubSeasonDicCriteria criteria;
}
