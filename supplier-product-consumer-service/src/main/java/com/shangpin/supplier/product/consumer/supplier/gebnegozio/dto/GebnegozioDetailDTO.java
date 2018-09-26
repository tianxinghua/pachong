package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class GebnegozioDetailDTO {
    private String id;
    private String sku;
    private String name;
    private String attribute_set_id;
    private String price;
    private String final_price;
    private String status;
    private String visibility;
    private String type_id;
    private String created_at;
    private String updated_at;
    private String weight;
    private ExtensionAttributes extension_attributes;
    private List<ProductLinks> product_links;
    private List<String> options;
    private List<MediaGalleryEntries> media_gallery_entries;
    private List<String> tier_prices;
    private List<CustomAttributes> custom_attributes;

}
