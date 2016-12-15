package com.shangpin.ephub.data.mysql.rule.pic.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRuleCriteria;

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
public class HubSupplierPicRuleCriteriaWithRowBounds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086212056781641590L;
	
	private HubSupplierPicRuleCriteria criteria;
	
	private RowBounds rowBounds;

}
