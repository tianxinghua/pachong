package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZRS on 2016/1/14.
 */
@Setter
@Getter
@ToString
public class QuitOrders implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @JSONField(name="SopSecondReturnOrderNo")
    private String sopSecondReturnOrderNo;
    @JSONField(name="SupplierNo")
    private String supplierNo;
    @JSONField(name="SopUserNo")
    private String sopUserNo;
    @JSONField(name="WarehouseNo")
    private String warehouseNo;
    @JSONField(name="WarehouseName")
    private String warehouseName;
    @JSONField(name="ReturnStatus")
    private String returnStatus;
    @JSONField(name="TotalQuantity")
    private String totalQuantity;
    @JSONField(name="LogisticsName")
    private String logisticsName;
    @JSONField(name="LogisticsOrderNo")
    private String logisticsOrderNo;
    @JSONField(name="SendOutTime")
    private String sendOutTime;
    @JSONField(name="ReceiveTime")
    private String receiveTime;
    @JSONField(name="CreateTime")
    private String createTime;
    @JSONField(name="secondReturnOrderDetails")
    private List<QuitOrderDetail> secondReturnOrderDetails;	//返货商品列表
}
