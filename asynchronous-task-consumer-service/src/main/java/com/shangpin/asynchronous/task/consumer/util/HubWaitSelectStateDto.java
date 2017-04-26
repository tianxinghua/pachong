package com.shangpin.asynchronous.task.consumer.util;

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
public class HubWaitSelectStateDto {

	private Long skuSupplierMappingId;
	private Long spuId;
	private Long skuId;
	private String spuNo;
	private String createUser;
}