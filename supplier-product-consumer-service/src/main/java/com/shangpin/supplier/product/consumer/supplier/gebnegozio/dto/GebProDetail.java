package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/17.
 */
@Getter
@Setter
public class GebProDetail {
    private String id;
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
    private ExtenAttrDetail extension_attributes;
    private List<ProductLinks> product_links;
    private List<String> options;
    private List<PictRes> media_gallery_entries;
    private List<String> tier_prices;
    private List<CustomAttributes> custom_attributes;
}
