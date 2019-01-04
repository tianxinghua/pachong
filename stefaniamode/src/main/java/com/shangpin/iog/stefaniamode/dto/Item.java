package com.shangpin.iog.stefaniamode.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/6/5.
 */
@XmlRootElement(name="item")
@Getter
@Setter
public class Item {
    private String item_id;
    private String item_size;
    private String barcode;
    private String market_price;
    private String sell_price;
    private String supply_price;
    private String discount_code;
    private String color;
    private String description;
    private String item_detail;
    private String designCode;
    private String farfetchId;
    private String stock;
    private String picture;
}
