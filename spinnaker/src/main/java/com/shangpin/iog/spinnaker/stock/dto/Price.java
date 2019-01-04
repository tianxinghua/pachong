package com.shangpin.iog.spinnaker.stock.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Price {
	
	private String productId;
	private String market_price;
	private String suply_price;

}
