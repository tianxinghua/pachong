package com.shangpin.ephub.data.mysql.slot.spusupplierunion.po;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class SlotSpuSupplier implements Serializable {
    /**
     * 主键
     */
    private Long slotSpuSupplierId;

    /**
     * slotspu主键
     */
    private Long slotSpuId;

    /**
     * slot编号
     */
    private String slotNo;


    /**
     * 供货商编号
     */
    private String supplierNo;

    /**
     * 供货商门户编号
     */
    private String supplierId;

    /**
     * SPUPENDING表主键
     */
    private Long spuPendingId;

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
     * 供货商操作标记     0：自己寄出    1: 另外的供货商已寄出
     */
    private Byte supplierOperateSign;



    /**
     * slotspu编号
     */
    private String slotSpuNo;

    /**
     * 货号
     */
    private String spuModel;

    /**
     * 品牌编号
     */
    private String brandNo;

    /**
     * 类目编号
     */
    private String categoryNo;

    /**
     * 二级品类编号（冗余）
     */
    private String secondCategoryNo;

    /**
     * 上市时间
     */
    private String marketTime;

    /**
     * 上市季节
     */
    private String season;

    /**
     * 上市季节_英文
     */
    private String seasonEn;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 产品描述
     */
    private String spuDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 0:未寄出 1：已寄出
     */
    private Byte spuState;

    /**
     * 颜色
     */
    private String hubColor;

    /**
     * 颜色码
     */
    private String hubColorNo;

    /**
     * 主图Url(尚品)
     */
    private String picUrl;

    /**
     * 信息来源
     */
    private Byte infoFrom;


    private Byte dataState;

    /**
     * 备注（修改的货号）
     */
    private String memo;

    /**
     * spu 是否有图片的标记
     */
    private Byte spuPicSign;

    /**
     * 供货商是否有图片的标记
     */
    private Byte supplierPicSign;





    private static final long serialVersionUID = 1L;










}