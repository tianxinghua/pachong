package com.shangpin.ephub.data.mysql.rule.pic.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRule;
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
public class HubSupplierPicRuleWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3278377015487500457L;

	private HubSupplierPicRule hubSupplierPicRule;
	
	private HubSupplierPicRuleCriteria criteria;
}
