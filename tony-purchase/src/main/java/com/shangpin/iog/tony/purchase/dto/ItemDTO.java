package com.shangpin.iog.tony.purchase.dto;

/**
 * Created by Administrator on 2015/9/28.
 */
public class ItemDTO {
    private String sku;
    private Integer qty;
    private double price;
    private Integer cur;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setPrice(String price) {
        if(price == null || "".equals(price.trim()))
            this.price = 0;
        this.price = Double.parseDouble(price);
    }

    public Integer getCur() {
        return cur;
    }

    public void setCur(Integer cur) {
        this.cur = cur;
    }
    public void setCur(String cur) {
        if(cur == null || "".equals(cur.trim()))
            this.cur = 0;
        this.cur = Integer.parseInt(cur);
    }
}
