package com.shangpin.ephub.data.mysql.slot.fixedproperty.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedPropertyCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSlotSpuFixedPropertyCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7255745601518578512L;

	private HubSlotSpuFixedPropertyCriteria criteria;
	
	private RowBounds rowBounds;
}
