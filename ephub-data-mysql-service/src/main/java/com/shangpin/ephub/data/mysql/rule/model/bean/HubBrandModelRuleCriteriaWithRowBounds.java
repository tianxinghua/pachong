package com.shangpin.ephub.data.mysql.rule.model.bean;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRuleCriteria;

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
public class HubBrandModelRuleCriteriaWithRowBounds implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2794962791263996915L;

	private HubBrandModelRuleCriteria criteria;
	
	private RowBounds rowBounds;
}
