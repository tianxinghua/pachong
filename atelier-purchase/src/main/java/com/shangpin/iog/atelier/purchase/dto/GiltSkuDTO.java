package com.shangpin.iog.atelier.purchase.dto;

import java.util.List;

/**
 * Created by sunny on 2015/8/5.
 */
public class GiltSkuDTO {
    private String id;
    private String product_id;
    private String product_look_id;
    private String locale;
    private String name;
    private List<ImageDTO> images;
    private List<AttributeDTO> attributes;
    private BrandDTO brand;
    private String country_code;
    private String[] product_codes;
    private String description;
    private String timestamp;
    private PricesDTO prices;
    private List<CategoryDTO> categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_look_id() {
        return product_look_id;
    }

    public void setProduct_look_id(String product_look_id) {
        this.product_look_id = product_look_id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDTO> attributes) {
        this.attributes = attributes;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public String[] getProduct_codes() {
        return product_codes;
    }

    public void setProduct_codes(String[] product_codes) {
        this.product_codes = product_codes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public PricesDTO getPrices() {
        return prices;
    }

    public void setPrices(PricesDTO prices) {
        this.prices = prices;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<ImageDTO> getImages() {
        return images;
    }
}
