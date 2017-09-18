package com.shangpin.ephub.data.mysql.sp.shippingpolicy.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicy;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicyCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpShippingPolicyWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private SpShippingPolicy spShippingPolicy;
	
	private SpShippingPolicyCriteria criteria;
}
