package com.shangpin.supplier.product.consumer.supplier.stefania.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:StefItem </p>
 * <p>Description: stefania供应商原始数据dto</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 上午11:19:36
 *
 */
@Getter
@Setter
public class StefItem {
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
