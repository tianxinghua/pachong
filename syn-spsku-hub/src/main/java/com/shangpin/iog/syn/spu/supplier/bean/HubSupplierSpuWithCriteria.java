package com.shangpin.iog.syn.spu.supplier.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.iog.syn.spu.supplier.po.HubSupplierSpu;
import com.shangpin.iog.syn.spu.supplier.po.HubSupplierSpuCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierSpuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2287536318041291041L;

	private HubSupplierSpu hubSupplierSpu;
	
	private HubSupplierSpuCriteria criteria;
	
}
