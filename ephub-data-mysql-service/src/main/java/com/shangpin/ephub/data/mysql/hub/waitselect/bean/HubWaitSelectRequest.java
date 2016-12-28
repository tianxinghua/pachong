package com.shangpin.ephub.data.mysql.hub.waitselect.bean;

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
public class HubWaitSelectRequest {
	
	private String supplierNo;
    private String spuModel;
    private String brandNo;
    private String categoryNo;
    private int spuState;
    private String startDate;
    private String endDate;
    private int spuSelectState;
}