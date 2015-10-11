package com.shangpin.iog.ice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2015/10/10.
 */
@Getter
@Setter
public class CategoryBrandDiscountDTO {
    public long CategoryBrandAgreementId;
    public long SopUserNo;
    public String CategoryNo;
    public String BrandNo;
    public String DiscountRate;
    /// <summary>
    /// 是否独家
    /// </summary>
    public int IsExclusive;
    public String CreateUserName;
    public String CreateTime;
    public String UpdateUserName;
    public String UpdateTime;
    public int DataState;
    /// <summary>
    /// 品类名称
    /// </summary>
    public String CategoryName;
    /// <summary>
    /// 品牌名称（中文+英文）
    /// </summary>
    public String BrandName;
    /// <summary>
    /// 供价折扣
    /// </summary>
    public String SupplyPriceRate;
    /// <summary>
    /// 是否直发
    /// </summary>
    public int IsDirectdelivery;

}
