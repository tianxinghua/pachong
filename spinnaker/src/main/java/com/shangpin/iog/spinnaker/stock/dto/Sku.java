package com.shangpin.iog.spinnaker.stock.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/5/28.
 */
@Setter
@Getter
public class Sku {


    private String item_id;
    private String item_size;
    private String barcode;
    private String color;
    private String stock;
    private String season;
    private String supply_price;
    private String MadeIn;
    private String last_modified;
    private List<String> pictures;
    private String country_size;

}
