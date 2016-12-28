package com.shangpin.ephub.client.data.mysql.hub.dto;

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
    private Byte spuState;
    private String startDate;
    private String endDate;
    private Byte spuSelectState;
    
    public Integer getPageNo() {
        return pageNo;
    }
    public void setPageNo(Integer pageNo) {
        this.pageNo = (pageNo-1)*this.pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize=pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }
	  
}