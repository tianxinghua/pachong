package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class ProductLinks {
    private String sku;
    private String link_type;
    private String linked_product_sku;
    private String linked_product_type;
    private String position;
    private List<ExtensionAttributes> extension_attributes = new ArrayList<ExtensionAttributes>();
}
