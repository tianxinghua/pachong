package com.shangpin.ep.order.module.orderapiservice.impl.dto.vietti2;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShippingAddress {

    private  String  firstname;
    private  String  lastname;
    private  String  street;
    private  String   city;
    private  String   country_id;
    private  String   region;
    private  String   postcode;
    private  String   telephone;


}