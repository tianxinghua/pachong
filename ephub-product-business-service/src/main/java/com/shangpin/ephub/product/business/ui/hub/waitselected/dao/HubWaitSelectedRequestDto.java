package com.shangpin.ephub.product.business.ui.hub.waitselected.dao;

import java.io.Serializable;

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
public class HubWaitSelectedRequestDto implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;
    /**
     * 供应商
     */
    private String supplierNo;
    private String productCode;
    private String brandNo;
    private String categoryNo;
    private String productState;
    private String startDate;
    private String endDate;
    private int pageNo;
    private int pageSize;

}