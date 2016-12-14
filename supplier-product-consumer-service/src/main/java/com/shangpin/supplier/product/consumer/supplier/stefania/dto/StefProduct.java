package com.shangpin.supplier.product.consumer.supplier.stefania.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:StefProduct </p>
 * <p>Description: stefania供应商原始数据dto</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 上午11:17:49
 *
 */
@Getter
@Setter
public class StefProduct {
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
    private StefItems items;

}
