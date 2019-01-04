package com.shangpin.api.airshop.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrderRS implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String total;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<DeliveryOrdersContent> deliverOrders;
}
