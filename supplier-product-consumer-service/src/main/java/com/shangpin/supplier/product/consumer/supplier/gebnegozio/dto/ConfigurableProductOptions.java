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
public class ConfigurableProductOptions {
    private String id;
    private String attributeId;
    private String label;
    private String position;
    private List<Values> values = new ArrayList<Values>();
    private String productId;
}
