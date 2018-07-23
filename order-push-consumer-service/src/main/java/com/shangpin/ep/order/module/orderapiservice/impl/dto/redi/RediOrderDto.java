package com.shangpin.ep.order.module.orderapiservice.impl.dto.redi;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2018/4/3.
 */
@Getter
@Setter
public class RediOrderDto implements Serializable{

    private ShippingInfo shippingInfo;
    
    private ShippingInfo shippingChInfo;

    private BillingInfo  billingInfo;

    private List<Product> products;

}
