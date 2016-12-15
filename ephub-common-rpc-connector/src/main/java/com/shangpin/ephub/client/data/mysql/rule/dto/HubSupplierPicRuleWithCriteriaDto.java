package com.shangpin.ephub.client.data.mysql.rule.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class HubSupplierPicRuleWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3278377015487500457L;

	private HubSupplierPicRuleDto hubSupplierPicRule;
	
	private HubSupplierPicRuleCriteriaDto criteria;
}
