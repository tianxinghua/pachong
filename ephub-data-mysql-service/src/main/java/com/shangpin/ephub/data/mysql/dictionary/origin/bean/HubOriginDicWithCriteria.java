package com.shangpin.ephub.data.mysql.dictionary.origin.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDic;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDicCriteria;

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
public class HubOriginDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2079472436364941833L;

	private HubOriginDic hubOriginDic;
	
	private HubOriginDicCriteria criteria;
}
