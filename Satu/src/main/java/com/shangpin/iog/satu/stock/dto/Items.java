package com.shangpin.iog.satu.stock.dto;

/**
 * Created by Administrator on 2015/5/28.
 */
public class Items {

    public String getNumber_of_items() {
        return number_of_items;
    }

    public void setNumber_of_items(String number_of_items) {
        this.number_of_items = number_of_items;
    }

    public Sku[] getItem() {
        return item;
    }

    public void setItem(Sku[] item) {
        this.item = item;
    }

    private String number_of_items;
    private Sku[] item;

}
