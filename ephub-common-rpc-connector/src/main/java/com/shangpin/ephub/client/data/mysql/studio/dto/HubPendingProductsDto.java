package com.shangpin.ephub.client.data.mysql.studio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/9.
 */
@Setter
@Getter
@ToString
public class HubPendingProductsDto implements Serializable {
    private static final long serialVersionUID = -4758627560356371288L;

    private int id;
    private int slotSpuId;  //slotspu主键
    private String slotNo;  //slot编号
    private String supplierNo;
    private String supplierId;
    private Long spuPendingId;
    private Long supplierSpuId;
    private int state;  //0:未寄出 1：已加入发货单  2：已发货
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;
    private int repeatMarker; //多家供货标记  0：独家  1：多家
    private int supplierOperateSign;//供货商操作标记     0：自己寄出    1: 另外的供货商已寄出
    private Long version ;
}
