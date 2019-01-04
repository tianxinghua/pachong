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
public class ReturnOrders implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONField(name = "sopReturnOrderNo")
    private String sopReturnOrderNo;	//返货单号
    @JSONField(name = "sopDeliveryOrderNo")
    private String sopDeliveryOrderNo;	//该返货单对应的发货单号
    @JSONField(name = "returnQuantity")
    private String returnQuantity;	//该返货单返回的商品数量
    @JSONField(name = "returnOrderStatus")
    private String returnOrderStatus;	//返货单状态 状态值描述：1=待收货，2=已完成
    @JSONField(name = "sendOutTime")
    private String sendOutTime;	//返货时间，以北京时间为准；北京时间=格林威治时间+8小时；格式：2015-01-17 14:00:00
    @JSONField(name = "createTime")
    private String createTime;	//返货时间，以北京时间为准；北京时间=格林威治时间+8小时；格式：2015-01-17 14:00:00
    @JSONField(name = "receiveTime")
    private String receiveTime;	//收货时间以北京时间为准；北京时间=格林威治时间+8小时；格式：2015-01-16 00:00:00
    @JSONField(name = "logisticsName")
    private String logisticsName;	//物流公司
    @JSONField(name = "logisticsOrderNo")
    private String logisticsOrderNo;	//物流单号
    @JSONField(name = "deliveryContacts")
    private String deliveryContacts;	//发货联系人
    @JSONField(name = "deliveryContactsPhone")
    private String deliveryContactsPhone;	//发货联系方式
    @JSONField(name = "deliveryAddress")
    private String deliveryAddress;	//发货地址
    @JSONField(name = "returnRemark")
    private String returnRemark;	//返货备注
    @JSONField(name = "warehouseNo")
    private String warehouseNo;	//返货仓库的仓库编号
    @JSONField(name = "warehouseName")
    private String warehouseName;	//返货仓库的仓库名称
    @JSONField(name = "returnOrderDetails")
    private List<ReturnOrderDetail> returnOrderDetails;	//返货商品列表
}
