package com.shangpin.ephub.data.mysql.sp.shippingpolicy.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicyCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpShippingPolicyCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7255745601518578512L;

	private SpShippingPolicyCriteria criteria;
	
	private RowBounds rowBounds;
}
