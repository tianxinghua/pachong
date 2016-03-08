package com.shangpin.iog.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceCountDetailDTO {
	
	private String supplierId;
	private String supplierName;
	private String discount;
	private String flag;
	private String state;
	@Override
	public String toString() {
		return "CountPriceDTO [supplierId=" + supplierId + ", supplierName="
				+ supplierName + ", discount=" + discount + ", flag=" + flag
				+ ", state=" + state + "]";
	}
	
	

}
