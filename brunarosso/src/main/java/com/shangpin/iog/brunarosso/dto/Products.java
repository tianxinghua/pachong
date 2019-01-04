package com.shangpin.iog.brunarosso.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sunny on 2015/7/10.
 */
@XmlRootElement(name="DocumentElement")
@XmlAccessorType(XmlAccessType.NONE)
public class Products {
    @XmlElement(name="Prodotti")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public  Products(){super();}
}
