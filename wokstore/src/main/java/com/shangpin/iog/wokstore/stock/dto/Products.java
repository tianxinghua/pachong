package com.shangpin.iog.wokstore.stock.dto;

/**
 * Created by Administrator on 2015/5/28.
 */
public class Products {

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Spu[] getProduct() {
        return product;
    }

    public void setProduct(Spu[] product) {
        this.product = product;
    }

    private String number;
    private Spu[] product;

}
