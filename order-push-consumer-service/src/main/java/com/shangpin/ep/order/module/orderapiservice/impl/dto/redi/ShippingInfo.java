package com.shangpin.ep.order.module.orderapiservice.impl.dto.redi;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/4/3.
 */
@Setter
@Getter
public class ShippingInfo implements Serializable {
    private String name;
    private String surname;
    private String company;
    private String vatcode;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private String email;
    private String phone;
    private String mobile;

}
