package com.shangpin.ephub.client.data.mysql.hub.dto;

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
public class HubWaitSelectRequestWithPageDto {
	
	private int pageNo;
	private int pageSize;
	private String supplierNo;
    private String spuModel;
    private String brandNo;
    private String categoryNo;
    private String startDate;
    private String endDate;
    private List<Byte> supplierSelectState;
}