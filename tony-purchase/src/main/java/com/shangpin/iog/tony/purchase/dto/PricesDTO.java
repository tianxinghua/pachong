package com.shangpin.iog.tony.purchase.dto;

/**
 * Created by sunny on 2015/8/6.
 */
public class PricesDTO {
    private MoneyDTO retail;
    private MoneyDTO sale;
    private MoneyDTO cost;

    public MoneyDTO getRetail() {
        return retail;
    }

    public void setRetail(MoneyDTO retail) {
        this.retail = retail;
    }

    public MoneyDTO getSale() {
        return sale;
    }

    public void setSale(MoneyDTO sale) {
        this.sale = sale;
    }

    public MoneyDTO getCost() {
        return cost;
    }

    public void setCost(MoneyDTO cost) {
        this.cost = cost;
    }
}
