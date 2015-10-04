package com.shangpin.iog.ice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/10.
 */
@Setter
@Getter
public class ICEOrderDetailDTO {
    private String  sku_id;
    private int quantity;

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
