package com.shangpin.ephub.client.data.mysql.hub.dto;

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
    
    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = (pageNo-1)*this.pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize=pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }
	  
}