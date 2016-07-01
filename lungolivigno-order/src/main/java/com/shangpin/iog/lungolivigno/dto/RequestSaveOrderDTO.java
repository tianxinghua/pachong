/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-31 9:41:17
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
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