package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Getter
@Setter
public  class SpProductOrgInfoEntity implements Serializable{
    /// <summary>
    /// 
    /// </summary>
    private long ProductOrgInfoId;
    /// <summary>
    /// 
    /// </summary>
    private String ProductOriginalName;//spuname 
    /// <summary>
    /// 
    /// </summary>
    private String ProductOriginalModel;//spumodel
    /// <summary>
    /// 
    /// </summary>
    private String SupplierProductModel;//供货商的货号
    /// <summary>
    /// 
    /// </summary>
    private Integer ProductOriginalUnit;//销售单位 （双 ，只） 存ID
    /// <summary>
    /// 
    /// </summary>
    private String CategoryOriginalNo;//品类
    /// <summary>
    /// 
    /// </summary>
    private String BrandOriginalNo;
    /// <summary>
    /// 
    /// </summary>
    private String SupplierNo;
    /// <summary>
    /// 
    /// </summary>
    private String ProductNo;//尚品的SPU编号
    /// <summary>
    /// 
    /// </summary>
    private String ProductOriginalFromId;//？
    /// <summary>
    /// 
    /// </summary>
    private Integer ProductOriginalFrom;//？固定值 来源
    /// <summary>
    /// 
    /// </summary>
    private Integer IsVCode;//?
    /// <summary>
    /// 
    /// </summary>
    private Integer  ProductOriginalSex;//性别
    /// <summary>
    /// 
    /// </summary>
    private Integer IsSupportCod;//是否支持货到付款
    /// <summary>
    /// 
    /// </summary>
    private Integer IsSupportReturn;//是否支付退货
    /// <summary>
    /// 
    /// </summary>
    private Integer IsSupportChange;//是否支持换货
    /// <summary>
    /// 
    /// </summary>
    private String SizeTmpNo;// 尺码模板
    /// <summary>
    /// 
    /// </summary>
    private String DepartmentNo;//部门编号
    /// <summary>
    /// 
    /// </summary>
    private String UserGroupNo;//部门
    /// <summary>
    /// 
    /// </summary>
    private Integer SkuDyaAttr;//?
    /// <summary>
    /// 
    /// </summary>
    private String StaffNo;//不需要
    /// <summary>
    /// 
    /// </summary>
    private String Merchant;//不需要
    /// <summary>
    /// 
    /// </summary>
    private Integer ProductType;// 填 1
    /// <summary>
    /// 
    /// </summary>
    private BigDecimal ProductMarketPrice;//
    /// <summary>
    /// 
    /// </summary>
    private Integer AuditStatus;//固定值
    /// <summary>
    /// 
    /// </summary>
    private String AuditUserName;// 不填
    /// <summary>
    /// 
    /// </summary>
    private String AuditTime;
    /// <summary>
    /// 
    /// </summary>
    private String Memo;

    /// <summary>
    /// 执行标准
    /// </summary>
    private String ExStandard;

    /// <summary>
    /// 安全技术类别
    /// </summary>
    private String SecurityCategory;
    /// <summary>
    /// 市场价币种
    /// </summary>
    private String MarketPriceCurreny;//必须填
    /// <summary>
    /// 销售类型
    /// </summary>
    private Integer SaleType;// 填 0
}
