package com.shangpin.ephub.data.mysql.picture.hub.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPicCriteria;

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
public class HubPicCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1826339610347447907L;

	private HubPicCriteria criteria;
	
	private RowBounds rowBounds;
}
