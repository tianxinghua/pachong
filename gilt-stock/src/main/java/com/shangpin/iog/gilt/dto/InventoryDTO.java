package com.shangpin.iog.gilt.dto;

/**
 * Created by sunny on 2015/8/10.
 */
public class InventoryDTO {
    private String sku_id;
    private String quantity;

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
