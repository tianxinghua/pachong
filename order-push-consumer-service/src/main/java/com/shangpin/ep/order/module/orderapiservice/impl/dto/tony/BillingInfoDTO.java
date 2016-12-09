package com.shangpin.ep.order.module.orderapiservice.impl.dto.tony;

/**
 * Created by Administrator on 2015/9/28.
 */
public class BillingInfoDTO {
    private double total;

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public void setTotal(String total) {
        if(total == null || "".equals(total.trim()))
            this.total = 0;
        this.total = Double.parseDouble(total);
    }

    private Integer paymentMethod;
    private AddressDTO address;


    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
