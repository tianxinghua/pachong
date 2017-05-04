package com.shangpin.ephub.product.business.ui.mapp.season.dto;

import java.util.List;

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
public class HubSupplierSeasonDicResponseWithPageDto {
	
	private int total;
	private List<HubSupplierSeasonDicResponseDto> list;

}
