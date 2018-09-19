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
public class ExtensionAttributes {
    private ConfigurableProductOptions configurable_product_options;
    private List<Integer> configurable_product_links = new ArrayList<Integer>();
}
