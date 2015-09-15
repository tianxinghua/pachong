package com.shangpin.iog.tessabit.stock.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@XmlRootElement(name="products")
@XmlAccessorType(XmlAccessType.NONE)
public class Products {

    @XmlElement(name="product")
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