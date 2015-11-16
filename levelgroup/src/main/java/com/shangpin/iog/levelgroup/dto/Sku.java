package com.shangpin.iog.levelgroup.dto;

/**
 * Created by Administrator on 2015/11/16.
 */
public class Sku {
    private String c_material;
    private Inventory inventory;

    public String getC_material() {
        return c_material;
    }

    public void setC_material(String c_material) {
        this.c_material = c_material;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
