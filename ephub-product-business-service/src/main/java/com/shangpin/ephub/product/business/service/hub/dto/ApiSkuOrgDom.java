package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Getter
@Setter
public class ApiSkuOrgDom implements Serializable {
    /// <summary>
    /// SPU序号，SCM系统自增序号
    /// </summary>
    private long ProductOrgInfoId;

    /// <summary>
    /// SKU序号，SCM系统自增序号
    /// </summary>
    private long SkuOrgInfoId;

    /// <summary>
    /// SKU名称
    /// </summary>
    private String SkuOrgName;

    /// <summary>
    /// 条形码
    /// </summary>
    private String BarCode;

    /// <summary>
    /// 供应商SKU编号
    /// </summary>
    private String SupplierSkuNo;

    /// <summary>
    /// 商品SKU
    /// </summary>
    private String SkuNo;

    /// <summary>
    /// 供应商编号
    /// </summary>
    private String SupplierNo;

    /// <summary>
    /// 市场价
    /// </summary>
    private BigDecimal MarketPrice;

    /// <summary>
    /// 市场价币种
    /// </summary>
    private String MarketPriceUnit;

    /// <summary>
    /// 色系
    /// </summary>
    private String ColourScheme; //?

    /// <summary>
    /// 筛选尺码
    /// </summary>
    private String ScreenSize;

    private String Memo;

    /// <summary>
    /// 尺码
    /// </summary>
    private List<String> ProductSize;

    /// <summary>
    /// 颜色
    /// </summary>
    private List<String> ProColor;

    /// <summary>
    /// 材质
    /// </summary>
    private List<String> MaterialList;

    /// <summary>
    /// 产地
    /// </summary>
    private List<PlaceOrigin> PlaceOriginList;
}
