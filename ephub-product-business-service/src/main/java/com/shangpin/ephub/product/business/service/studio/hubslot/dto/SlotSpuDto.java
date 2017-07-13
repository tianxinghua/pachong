package com.shangpin.ephub.product.business.service.studio.hubslot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by loyalty on 17/6/29.
 */
@Getter
@Setter
public class SlotSpuDto {
    private Integer pageIndex;
    private Integer pageSize;
    private String supplierNo;
    private String spuModel;
    private String hubCategoryNo;
    private String hubBrandNo;
    private String studioName;

    private boolean isHavePic;

    private List<SlotSpuSupplierDto> spuSupplierDtos;


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

    private String updateTime;

    private String createTime;
    /**
     * 0:未寄出 1：加入发货单  2：已寄出 3:不需要处理
     */
    private Integer spuState;



    /**
     * 操作人
     */
    private String operator;
}
