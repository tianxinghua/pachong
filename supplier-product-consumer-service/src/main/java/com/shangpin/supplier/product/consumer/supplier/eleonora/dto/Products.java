package com.shangpin.supplier.product.consumer.supplier.eleonora.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by loyalty on 15/6/5.
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

    @Override
    public String toString() {
        return "Products{" +
                "products=" + products +
                '}';
    }
}
