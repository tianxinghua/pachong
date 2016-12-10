package com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:Spu </p>
 * <p>Description: spinnaker原始spu</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 下午3:34:33
 *
 */
@Getter
@Setter
public class Spu {

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
