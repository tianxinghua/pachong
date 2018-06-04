package com.shangpin.ephub.client.product.business.mapp.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class HubSupplierCategoryDicRequestDto {
	
	private Long supplierCategoryDicId;
	private String supplierNo;
	private String supplierId;
	private int pageNo;
	private int pageSize;
	private String hubCategoryNo;
	private String supplierCategory;
	private String supplierGender;
	private Byte categoryType;
	private String updateUser;
	private byte refreshDicType;
	
	public static void main(String[] args) {
		
		Map<String,String> map = new HashMap<>();
		map.put(" .5", "1");
		if(map.containsKey(" .5")){
			System.out.println(map.get(" .5"));
		}
	}

}
