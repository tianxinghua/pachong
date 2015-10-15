package com.shangpin.iog.articoli.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by huxia on 2015/10/15.
 */
public class Items {
    private String quantity;//Item的个数
    private Item[] item;

    public Item[] getItem() {
        return item;
    }

    public void setItem(Item[] item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



}
