package com.shangpin.iog.tony.purchase.dto;

/**
 * Created by Administrator on 2015/9/28.
 */
public class ShippingInfoDTO {
    private String fees;

    public ShippingAddressDTO getAddress() {
        return address;
    }

    public void setAddress(ShippingAddressDTO address) {
        this.address = address;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    private ShippingAddressDTO address;
}
