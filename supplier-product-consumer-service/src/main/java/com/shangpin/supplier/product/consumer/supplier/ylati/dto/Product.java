package com.shangpin.supplier.product.consumer.supplier.ylati.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class Product {
    private String id;
    private String title;
    private String body_html;
    private String vendor;
    private String product_type;
    private String created_at;
    private String handle;
    private String updated_at;
    private String published_at;
    private String template_suffix;
    private String tags;
    private String published_scope;
    private List<SkuPro> variants;
    private List<Image>  images;
    private String image;











}
