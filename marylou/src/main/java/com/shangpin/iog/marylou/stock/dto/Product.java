package com.shangpin.iog.marylou.stock.dto;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Getter
@Setter
@XmlRootElement(name="product")
public class Product {
    private String productId;
    private String description;
    private String name;
    private String brand;
    private String type;
    private String color;
    private String season;
    private String category;
    private String detail;
    private String material;
    private String gender;
    private Items items ;
}
