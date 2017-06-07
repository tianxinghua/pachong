package com.shangpin.ephub.data.mysql.slot.supplier.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplierCriteria;

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
public class HubSlotSpuSupplierWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSlotSpuSupplier hubSlotSpuSupplier;
	
	private HubSlotSpuSupplierCriteria criteria;
}
