package com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetailCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpShippingPolicyDetailCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7255745601518578512L;

	private SpShippingPolicyDetailCriteria criteria;
	
	private RowBounds rowBounds;
}
