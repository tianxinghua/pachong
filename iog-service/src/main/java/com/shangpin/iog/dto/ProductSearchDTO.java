package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/5/27.
 */
@Getter
@Setter
public class ProductSearchDTO {
    private String startDate;
    private String endDate;
    private Integer pageIndex;
    private Integer pageSize;
    private String category;
    private String  supplier;
    private String  supplierName;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }




    public String getSupplier() { return supplier; }

    public void setSupplier(String supplier) { this.supplier = supplier; }
}
