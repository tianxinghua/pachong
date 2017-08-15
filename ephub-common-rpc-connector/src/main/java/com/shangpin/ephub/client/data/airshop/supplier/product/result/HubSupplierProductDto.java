package com.shangpin.ephub.client.data.airshop.supplier.product.result;

import java.util.Date;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HubSupplierProductDto extends HubSupplierSkuDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5730174693722988096L;
	/**
     * 主键
     */
    private Long supplierSpuId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商Spu编号
     */
    private String supplierSpuNo;

    /**
     * 货号
     */
    private String supplierSpuModel;

    /**
     * 供应商原始商品名称
     */
    private String supplierSpuName;

    /**
     * 商品颜色
     */
    private String supplierSpuColor;

    /**
     * 性别
     */
    private String supplierGender;

    /**
     * 供应商类目编号
     */
    private String supplierCategoryno;

    /**
     * 供应商类目名称
     */
    private String supplierCategoryname;

    /**
     * 供应商品牌编号
     */
    private String supplierBrandno;

    /**
     * 供应商品牌名称
     */
    private String supplierBrandname;

    /**
     * 供应商季节编号
     */
    private String supplierSeasonno;

    /**
     * 供应商季节名称
     */
    private String supplierSeasonname;

    /**
     * 0:不存在  1：存在
     */
    private Byte isexistpic;

    /**
     * 商品材质
     */
    private String supplierMaterial;

    /**
     * 商品产地
     */
    private String supplierOrigin;

    /**
     * 商品描述
     */
    private String supplierSpuDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * =0 信息不完整 =1 信息已完整
     */
    private Byte infoState;

    /**
     * 备注
     */
    private String memo;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;
}