package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubMaterialImportDto {
    private String materialMappingId;
    private String hubMaterial;
    private String supplierMaterial;
    private String mappingLevel;
    private Date createTime;
    private Date updateTime;
    private String updateUser;
    public String getMaterialMappingId() {
        return materialMappingId;
    }

    public void setMaterialMappingId(String materialMappingId) {
        this.materialMappingId = materialMappingId;
    }

    public String getHubMaterial() {
        return hubMaterial;
    }

    public void setHubMaterial(String hubMaterial) {
        this.hubMaterial = hubMaterial;
    }

    public String getSupplierMaterial() {
        return supplierMaterial;
    }

    public void setSupplierMaterial(String supplierMaterial) {
        this.supplierMaterial = supplierMaterial;
    }

    public String getMappingLevel() {
        return mappingLevel;
    }

    public void setMappingLevel(String mappingLevel) {
        this.mappingLevel = mappingLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
