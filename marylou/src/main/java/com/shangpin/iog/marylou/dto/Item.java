package com.shangpin.iog.marylou.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Getter
@Setter
@XmlRootElement(name="item")
public class Item {
    private String itemId;
    private String item_size;
    private String stock;
    private String price_currency;
    private String supply_price;
    private String picture;
}
