package com.shangpin.iog.tony.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
	private String product_id;
    private String producer_id;
    private String type;
    private String season;
    private String product_name;
    private String description;
    private String category;
    private String product_detail;
    private String product_MadeIn;
    private String product_Material;
    private String product_Measure;
    private String url;
    private String supply_price;
    private String CarryOver;
    private String product_Note;
    private Items items;
}
