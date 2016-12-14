package com.shangpin.ephub.data.mysql.spu.supplier.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;

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
public class HubSupplierSpuWithCriteria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2287536318041291041L;

	private HubSupplierSpu hubSupplierSpu;
	
	private HubSupplierSpuCriteria criteria;
	
}
