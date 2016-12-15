package com.shangpin.ephub.data.mysql.dictionary.color.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItem;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItemCriteria;

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
public class HubColorDicItemWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 912241422235144650L;
	
	private HubColorDicItem hubColorDicItem;
	
	private HubColorDicItemCriteria criteria;

}
