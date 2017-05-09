package com.shangpin.iog.theClucher.purchase.order;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/9/28.
 */

@Setter
@Getter
public class ItemDTO {
    private String skuID;
    private Integer qty;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
