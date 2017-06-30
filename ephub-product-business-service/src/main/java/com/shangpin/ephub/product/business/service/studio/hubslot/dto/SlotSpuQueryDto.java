package com.shangpin.ephub.product.business.service.studio.hubslot.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 17/6/29.
 */
@Getter
@Setter
public class QueryDto {
    private Integer pageIndex;
    private Integer pageSize;
    private String supplierNo;
    private String spuModel;
    private String hubCategoryNo;
    private String hubBrandNo;


    /**
     * 开始更新时间
     */
    private String statTime;
    /**
     * 结束更新时间
     */
    private String endTime;


    private String createUser;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * HUB品类
     */
    private String categoryName;

    /**
     * 导出第几页
     */
    private String exportPageIndex;
    /**
     * 导出每页的商品数量
     */
    private String exportPageSize;

    /**
     * spu主键
     */
    private Long slotSpuId;



    /**
     * 商品状态
     */
    private String spuState;

    /**
     * 操作人
     */
    private String operator;
}
