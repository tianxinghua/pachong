package com.shangpin.ephub.data.mysql.slot.fixedproperty.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedProperty;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedPropertyCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSlotSpuFixedPropertyWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private HubSlotSpuFixedProperty hubSlotSpuFixedProperty;
	
	private HubSlotSpuFixedPropertyCriteria criteria;
}
