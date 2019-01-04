package com.shangpin.iog.tony.dto;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/9/21.
 */
@Setter
@Getter
@ToString
public class Items {

    private Object _id;
    private String cur;
    private String color;
    private String sex;
    private String title;
    private Object cat_ids;
    private String shopCatId;
    private String season;
    private String shopId;
    private String sku;
    private String brand;
    private String barcode;
    private String age;
    private String desc;
    private String size;
    private String desc_en;
    private String title_en;
    private String color_en;
    private String mnf_code;
    private String sku_parent;
    private Object ap_id;
    private String qty;
    private String stock_price;
    private String[] images;
}
