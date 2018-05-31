package com.shangpin.ephub.product.business.ui.task.dic.DicExportDto;

import java.util.Date;

public class BrandRequestDTO {
    protected Integer pageNo = 1;
    protected Integer startRow;
    protected Integer pageSize = 10;
    protected String  supplierId;
    protected String  supplierBrand;
    protected String hubBrand;
    protected String createUser;


    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierBrand() {
        return supplierBrand;
    }

    public void setSupplierBrand(String supplierBrand) {
        this.supplierBrand = supplierBrand;
    }

    public String getHubBrand() {
        return hubBrand;
    }

    public void setHubBrand(String hubBrand) {
        this.hubBrand = hubBrand;
    }

    @Override
    public String toString() {
        return "BrandRequestDTO{" +
                "pageNo=" + pageNo +
                ", startRow=" + startRow +
                ", pageSize=" + pageSize +
                ", supplierId='" + supplierId + '\'' +
                ", supplierBrand='" + supplierBrand + '\'' +
                ", hubBrand='" + hubBrand + '\'' +
                ", createUser='" + createUser + '\'' +
                '}';
    }
}
