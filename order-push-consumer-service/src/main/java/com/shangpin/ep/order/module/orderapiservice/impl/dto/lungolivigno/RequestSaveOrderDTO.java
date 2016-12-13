package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveOrderDTO {

    private String ID;
    private String OrderDate;
    private Billingcustomer BillingCustomer;
    private Shippingcustomer ShippingCustomer;
    private List<Rows> Rows;
    private boolean HasTax;
    private int Status;
    private int ImportTax;
    private int Shipping;
    private int Discount;
    private String PaymentMode;
}