package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2016/1/11.
 */
@Setter
@Getter
public class LogisticsDTO {

    private BigInteger id;

    private String supplierId;//供货商门户编号
    private String orderNo;//订单编号
    private String purchaseNo;//采购单
    private String purchaseDetailNo;//采购单明细单号
    private String logisticsCompany;// 物流公司
    private String trackNumber;//物流单号
    private String shippedDate;//发货时间
    private Date createDate;//创建时间
    private Date updateDate;//更新时间
    private String spInvoice;//尚品的发货单号
    private String memo;//备注
    private List<String> purchaseDetailList;//临时信息 与数据库无关


}
