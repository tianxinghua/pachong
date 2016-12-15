package com.shangpin.ephub.client.data.mysql.rule.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.common.dto.RowBoundsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:HubBrandModelRuleCriteriaWithRowBounds.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午7:41:59
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubBrandModelRuleCriteriaWithRowBoundsDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2794962791263996915L;

	private HubBrandModelRuleCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
