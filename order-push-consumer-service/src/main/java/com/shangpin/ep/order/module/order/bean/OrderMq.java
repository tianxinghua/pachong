package com.shangpin.ep.order.module.order.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by lizhongren on 2016/10/24.
 */
@Getter
@Setter
@ToString
public class OrderMq {
    private String messageId;

    private String orderNo;
    //下单:CreateOrder , 取消：CancelOrder, 支付：S
    private String syncType;

    private List<OrderDetailMq> syncDetailDto;


}
