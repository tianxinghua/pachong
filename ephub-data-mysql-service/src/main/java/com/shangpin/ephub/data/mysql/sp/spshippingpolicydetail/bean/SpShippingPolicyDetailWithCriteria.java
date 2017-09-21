package com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetail;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetailCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpShippingPolicyDetailWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private SpShippingPolicyDetail spShippingPolicyDetail;
	
	private SpShippingPolicyDetailCriteria criteria;
}
