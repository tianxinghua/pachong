package com.shangpin.ephub.data.mysql.dictionary.color.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDic;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicCriteria;

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
public class HubColorDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4759716339997637156L;

	private HubColorDic hubColorDic;
	
	private HubColorDicCriteria hubColorDicCriteria;
}
