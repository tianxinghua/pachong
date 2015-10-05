package com.shangpin.iog.gherardi.stock.dto;
import java.util.List;


/**
 * Created by loyalty on 15/6/5.
 */


public class Products {

    private List<Product> products;


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Products(){
        super();
    }
}
