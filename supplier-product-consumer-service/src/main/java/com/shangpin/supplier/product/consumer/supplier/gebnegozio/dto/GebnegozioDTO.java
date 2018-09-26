package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class GebnegozioDTO implements Serializable{

    private static final long serialVersionUID = -947506668494381493L;
    private String id;

    private String supplierId;

    private String sku;
    private String spu;
    private String name;
    private String attribute_set_id;
    private BigDecimal price;
    private BigDecimal final_price;
    private String status;
    private String visibility;
    private String type_id;
    private String created_at;
    private String updated_at;
    private String weight;
    private List<ExtensionAttributes> extension_attributes = new ArrayList<ExtensionAttributes>();
    private List<ProductLinks> product_links = new ArrayList<ProductLinks>();
    private List<String> tier_prices = new ArrayList<String>();
    private List<CustomAttributes> custom_attributes = new ArrayList<CustomAttributes>();
}
