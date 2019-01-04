package com.shangpin.iog.railSoAtelier.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Getter
@Setter
@XmlRootElement(name="products")
@XmlAccessorType(XmlAccessType.NONE)
public class Products {
    public List<Product> getProducts() {
        return products;
    }

    @XmlElement(name="product")
    private List<Product> products;
}