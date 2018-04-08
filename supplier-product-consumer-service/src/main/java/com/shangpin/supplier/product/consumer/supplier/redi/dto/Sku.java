package com.shangpin.supplier.product.consumer.supplier.redi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/28.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Sku implements Serializable {



    private String item_size_id;
    private String item_size;
    private String item_stock;
    private String item_size_system;


}
