package com.shangpin.ephub.product.business.ui.task.dic.DicExportDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupplierCategroyDicCriteriaDto {
    protected Integer pageNo = 1;
    protected Integer startRow;
    protected Integer pageSize = 10;
    protected String fields;
    protected String CreateName;
    protected String supplierId;
    protected String supplierCategory;
    protected String supplierGender;
    protected String supplierCategoryType;
    protected String startTime;
    protected String endTime;
    protected String updateUser;

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getCreateName() {
        return CreateName;
    }

    public void setCreateName(String createName) {
        CreateName = createName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCategory() {
        return supplierCategory;
    }

    public void setSupplierCategory(String supplierCategory) {
        this.supplierCategory = supplierCategory;
    }

    public String getSupplierGender() {
        return supplierGender;
    }

    public void setSupplierGender(String supplierGender) {
        this.supplierGender = supplierGender;
    }

    public String getSupplierCategoryType() {
        return supplierCategoryType;
    }

    public void setSupplierCategoryType(String supplierCategoryType) {
        this.supplierCategoryType = supplierCategoryType;
    }

    @Override
    public String toString() {
        return "SupplierCategroyDicCriteriaDto{" +
                "pageNo=" + pageNo +
                ", startRow=" + startRow +
                ", pageSize=" + pageSize +
                ", fields='" + fields + '\'' +
                ", CreateName='" + CreateName + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierCategory='" + supplierCategory + '\'' +
                ", supplierGender='" + supplierGender + '\'' +
                ", supplierCategoryType='" + supplierCategoryType + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}