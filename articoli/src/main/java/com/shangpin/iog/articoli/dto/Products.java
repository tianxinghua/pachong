package com.shangpin.iog.articoli.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by huxia on 2015/10/15.
 */

public class Products {
    private String number;
    private Product[] products;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }


}
