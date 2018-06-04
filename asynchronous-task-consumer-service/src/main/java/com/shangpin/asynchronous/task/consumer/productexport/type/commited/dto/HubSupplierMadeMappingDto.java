package com.shangpin.asynchronous.task.consumer.productexport.type.commited.dto;

import java.io.Serializable;

public class HubSupplierMadeMappingDto implements Serializable{
    protected Integer pageNo = 1;
    protected Integer startRow;
    protected Integer pageSize = 10;
    // 3代表表中的产地
    protected Byte type = 3;
    protected String fields;
    protected String supplierVal;
    protected String hubVal;
    protected String  mappingType;
    protected String CreateUser;
    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getPageNo() {
        return pageNo;
    }
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getSupplierVal() {
        return supplierVal;
    }

    public void setSupplierVal(String supplierVal) {
        this.supplierVal = supplierVal;
    }

    public String getHubVal() {
        return hubVal;
    }

    public void setHubVal(String hubVal) {
        this.hubVal = hubVal;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    @Override
    public String toString() {
        return "HubSupplierMadeMappingDto{" +
                "pageNo=" + pageNo +
                ", startRow=" + startRow +
                ", pageSize=" + pageSize +
                ", fields='" + fields + '\'' +
                ", supplierVal='" + supplierVal + '\'' +
                ", hubVal='" + hubVal + '\'' +
                ", mappingType='" + mappingType + '\'' +
                '}';
    }
}
