package com.shangpin.iog.parisi.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/6/5.
 */

@XmlRootElement(name="ns_product")
@Getter
@Setter
public class Product {
    private String idProduct;
    private String product;
    private String sku;
    private String gender;
    private String idGender;
    private String category;
    private String idCategory;
    private String season;
    private String idSeason;
    private String brand;
    private String idBrand;
    private String composition;
    private String idComposition;
    private String country;
    private String idCountry;
    private String name;
    private String price;
    private String supplierPrice;
    private String stock;
    private String barcode;
    private String productCode;
    private String color;
    private String idColor;
    private String currency;
    private String size;
    private String idSize;
    private String specification;
    private String images;



}
