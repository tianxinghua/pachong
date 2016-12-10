package com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:Sku </p>
 * <p>Description: spinnaker原始sku </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 下午3:45:09
 *
 */
@Setter
@Getter
public class Sku {

    private String item_id;
    private String item_size;
    private String barcode;
    private String color;
    private String stock;
    private String last_modified;
    private List<String> pictures;
    private Price price;

}
