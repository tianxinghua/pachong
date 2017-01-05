package com.shangpin.ephub.client.data.mysql.hub.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HubWaitSelectResponseDto {
	  /**
     * hubspuno
     */
	private Long skuId;
	private Long spuId;
    private String spuNo;

    /**
     * 颜色
     */
    private String color;

    /**
     * 尺码
     */
    private String skuSize;
    private String skuSizeId;
    /**
     * hubskuno
     */
    private String skuNo;
    
    /**
     * 货号
     */
    private String spuModel;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 类目编号
     */
    private String categoryNo;

    /**
     * 品牌编号
     */
    private String brandNo;

    /**
     * 上市时间
     */
    private String marketTime;

    /**
     * 颜色
     */
    private String hubColor;

    /**
     * 颜色码
     */
    private String hubColorNo;

    /**
     * 上市季节
     */
    private String season;

    /**
     * 产品描述
     */
    private String spuDesc;


    /**
     * 商品状态
     */
    private Byte spuState;

    /**
     * 商品选品状态
     */
    private Byte spuSelectState;

    /**
     * 新品类型
     */
    private Byte newType;

    /**
     * 主图Url(尚品)
     */
    private String picUrl;

    private String origin;

    /**
     * 材质
     */
    private String material;

    /**
     * 信息来源
     */
    private Byte infoFrom;
    /**
     * 主键
     */
    private Long skuSupplierMappingId;

    /**
     * 供应商SkuNo
     */
    private String supplierSkuNo;

    /**
     * 条码
     */
    private String barcode;
    /**
     * 新品类型
     */
    private Byte newSpuType;

    /**
     * 供应商选择状态
     */
    private Byte supplierSelectState;

    /**
     * 供应商编号
     */
    private String supplierNo;
    private String supplierId;
    
    private String isNewSupplier;
    private String supplierSpuModel;
    private Date updateTime;
	    
	    
}