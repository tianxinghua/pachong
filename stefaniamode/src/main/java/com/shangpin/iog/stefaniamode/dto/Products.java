package com.shangpin.iog.stefaniamode.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/6/5.
 */

@XmlRootElement(name="products")
@XmlAccessorType(XmlAccessType.NONE)
@Getter
@Setter
public class Products {

    @XmlElement(name="product")
    private List<Product> products;
}
