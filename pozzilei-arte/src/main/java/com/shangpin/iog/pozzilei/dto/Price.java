package com.shangpin.iog.pozzilei.dto;

/**
 * Created by huxia on 2015/7/22.
 */
public class Price {
    private String productId;
    private String market_price;
    private String suply_price;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getSuply_price() {
        return suply_price;
    }

    public void setSuply_price(String suply_price) {
        this.suply_price = suply_price;
    }
}
