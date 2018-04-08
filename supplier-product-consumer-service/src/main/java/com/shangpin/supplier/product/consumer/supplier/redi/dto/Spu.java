package com.shangpin.supplier.product.consumer.supplier.redi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Spu implements Serializable {


    // skuno
    private String item_id;
    //货号
    private String item_sku;
    private String item_name;
    private String item_altName;
    private String item_brand;
    private String item_brand_id;
    private String item_gender;
    private String item_category;
    private String item_groups;
    private String item_season;
    private String item_color;
    private String item_color_second;
    private String item_hardware_color;
    private String item_material;//材质 "Acetate, Viscose"
    private String item_detailed_composition;//材质 73% Acetate, 27% Viscose
    private String item_dimension;
    private String item_heel;
    private String item_description;

    private String item_shortDescription;
    private String item_detailed_name;
    private String item_longDescription;
    private String item_madein;
    private String item_retail_price;//零售价 市场价
    private String item_supply_price;//供价
    private String item_discount;//折扣
    private String item_temporary_discount_start_date;
    private String item_temporary_discount_end_date;

    private String item_temporary_supply_price;
    private String item_temporary_discount;

    private List<String> item_images;

    private String item_created;
    private String item_update;

    private List<Sku> item_sizes;

}
