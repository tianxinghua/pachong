package com.shangpin.ephub.client.data.mysql.rule.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:HubBrandModelRuleWithCriteria.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午7:45:36
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubBrandModelRuleWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1995464754112731582L;
	
	private HubBrandModelRuleDto hubBrandModelRule;
	
	private HubBrandModelRuleCriteriaDto criteria;

}
