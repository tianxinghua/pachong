package com.shangpin.iog.bernardelli.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/6/5.
 */


@Setter
@Getter
@ToString
public class Product {
    private String sku;
    private String configurable_sku;
    private String model;
    private String item_code;
    private String barcode;
    private String name;
    private String short_description;
    private String  description;
    private String color;
    private String  size;
    private String size_type;
    private String  designer;
    private String sector;
    private String  style;
    private String  fabric;
    private String  pattern;
    private String  season_acronym;
    private String  season_type;
    private String  composition;
    private String  made_in;
    private String  sales_type;
    private String  price;
    private String special_price;
    private String stock;
    private String  main_image;
    private Map<String,String> other_images;
    private List<String> categories;


}
