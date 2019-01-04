package com.shangpin.api.airshop.product.d.hub;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class HubPageDTO {
	
	private List<HubProductDTO> list;
	
	private int count;

}
