package com.shangpin.ep.order.module.orderapiservice.impl.dto.vietti2;


import com.shangpin.ep.order.module.orderapiservice.impl.dto.efashion.Item;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.vietti2.BillingAddress;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.vietti2.ShippingAddress;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class OrderParam {
    private ShippingAddress shipping_address;
    private BillingAddress billing_address;
    private Map<String,String> products;

}
