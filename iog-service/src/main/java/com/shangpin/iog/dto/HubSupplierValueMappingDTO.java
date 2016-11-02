package com.shangpin.iog.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HubSupplierValueMappingDTO implements Serializable {

    private BigInteger id;

    private Long colMappingId;

    private String supplierId;

    private String supplierValueNo;

    private String supplierValueParentNo;

    private String supplierValue;

    private String spValueNo;

    private String spValue;

    /**
     * 1、品牌 2、品类 3、产地 等
     */
    private Integer spValueType;

    /**
     * 1、单字段完整映射   
            2、组合映射
            3、部分映射
     */
    private Integer mappingType;

    private Integer mappingStatus;

    private Short sortValue;

    private Integer dataState;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private String memo;

    private static final long serialVersionUID = 1L;



}