package com.shangpin.iog.papini.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by loyalty on 15/6/5.
 */

@XmlRootElement(name="product")
public class Product {
    private String productId;
    private String product_name;
    private String season_code;
    private String main_category;
    private String description;
    private String category;
    private String product_brand;
    private String made_in;
    private String product_detail;
    private String product_material;
    private String url;
    private String supply_price;
    private String producer_id;
    private Items items;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSeason_code() {
        return season_code;
    }

    public void setSeason_code(String season_code) {
        this.season_code = season_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
    }

    public String getProduct_material() {
        return product_material;
    }

    public void setProduct_material(String product_material) {
        this.product_material = product_material;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSupply_price() {
        return supply_price;
    }

    public void setSupply_price(String supply_price) {
        this.supply_price = supply_price;
    }

    public String getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(String producer_id) {
        this.producer_id = producer_id;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    //    public List<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(List<Item> items) {
//        this.items = items;
//    }
    
    public Product(){
        super();
    }

	public String getMade_in() {
		return made_in;
	}

	public void setMade_in(String made_in) {
		this.made_in = made_in;
	}

	public String getMain_category() {
		return main_category;
	}

	public void setMain_category(String main_category) {
		this.main_category = main_category;
	}

}
