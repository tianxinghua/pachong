package com.shangpin.iog.railSoAtelier.dto;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Getter
@Setter
@XmlRootElement(name="product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {
    private String manufacturer_name;
    private String category_default_id;
    private String price;
    private String ean13;
    private String reference;
    private String manufacturer_id;
    private String product_id;
    private String descriptions;
    private String name;
    private String link_rewrite;
    private String description_short;
    
    private String product_category_tree;
    private String price_shipping;
    private String price_sale;
    private String product_url;
    private String shangpin ;
    private Image images ;
    private Category default_category;
  
    
}
