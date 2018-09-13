package com.shangpin.supplier.product.consumer.supplier.vietti.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CsvDTO {

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
    private int stock;
    private String  main_image;
    private Map<String,String> other_images;
    private List<String> categories;
}
