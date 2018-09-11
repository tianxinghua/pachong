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
    private String linkType;
    private String linkedProductSku;
    private String linkedProductType;
    private String position;
    private List<ExtensionAttributes> extensionAttributes = new ArrayList<ExtensionAttributes>();
}
