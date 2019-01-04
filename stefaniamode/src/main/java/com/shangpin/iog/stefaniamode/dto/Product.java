package com.shangpin.iog.stefaniamode.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/6/5.
 */

@XmlRootElement(name="product")
@Getter
@Setter
public class Product {
    private String productId;
    private String product_name;
    private String season_code;
    private String main_category;
    private String category;
    private String description;
    private String product_brand;
    private String made_in;
    private String product_detail;
    private String product_material;
    private String url;
    private String supply_price;
    private String producer_id;
    private Items items;

}
