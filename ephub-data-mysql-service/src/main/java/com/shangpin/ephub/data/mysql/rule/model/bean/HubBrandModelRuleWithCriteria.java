package com.shangpin.ephub.data.mysql.rule.model.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRule;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRuleCriteria;

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
public class HubBrandModelRuleWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1995464754112731582L;
	
	private HubBrandModelRule hubBrandModelRule;
	
	private HubBrandModelRuleCriteria criteria;

}
