package com.shangpin.ephub.data.mysql.hub.waitselect.bean;

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
public class HubWaitSelectRequest {
	
	private String supplierNo;
    private String spuModel;
    private String brandNo;
    private String categoryNo;
    private String spSkuNo;
    private List<String> spSkuNoList;
    private String startDate;
    private String endDate;
    private String createUser;
    private String operateUser;
    private List<Byte> supplierSelectState;
    private Byte pictureState;
}