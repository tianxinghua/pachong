package com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.result;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/20.
 */
@Getter
@Setter
public class HubSlotSpuSupplierExtend {
    private static final long serialVersionUID = 1566994724018863473L;
    /**
     * 主键
     */
    private Long slotSpuSupplierId;
    /**
     * 供货商门户编号
     */
    private String supplierId;

    /**
     * 供货商编号
     */
    private String supplierNo;

    /**
     * 供货商SPU主键
     */
    private Long supplierSpuId;

    /**
     * 0:未寄出 1：已加入发货单  2：已发货
     */
    private Byte state;

    /**
     * 多家供货标记  0：独家  1：多家
     */
    private Byte repeatMarker;

    /**
     * SPUPENDING表主键
     */
    private Long spuPendingId;

    /**
     * 供货商操作标记     0：自己寄出    1: 另外的供货商已寄出
     */
    private Byte supplierOperateSign;

    private Date createTime;

    /**
     * 供货商商品编号
     */
    private String supplierSpuNo;

    /***
     * 货号
     */
    private String supplierSpuModel;
    /**
     * 商品名称
     * */
    private String supplierSpuName;

    private String categoryName;

    private String brandName;

    private String seasonName;

    /**
     * slotspu主键
     */
    private Long slotSpuId;

    /**
     * slot编号
     */
    private String slotNo;

    /**
     * slotspu编号
     */
    private String slotSpuNo;
}
