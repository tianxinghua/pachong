package com.shangpin.ephub.data.mysql.slot.appendix.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendix;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendixCriteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSlotSpuAppendixWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSlotSpuAppendix hubSlotSpuAppendix;
	
	private HubSlotSpuAppendixCriteria criteria;
}
