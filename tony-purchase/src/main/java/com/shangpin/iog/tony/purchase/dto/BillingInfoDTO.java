package com.shangpin.iog.tony.purchase.dto;

/**
 * Created by Administrator on 2015/9/28.
 */
public class BillingInfoDTO {
    private String total;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private String paymentMethod;
    private AddressDTO address;


    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
