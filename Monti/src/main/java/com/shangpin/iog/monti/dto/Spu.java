package com.shangpin.iog.monti.dto;

/**
 * Created by Administrator on 2015/5/28.
 */
public class Spu {

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(String producer_id) {
        this.producer_id = producer_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
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

    
    public String getProduct_MadeIn() {
		return product_MadeIn;
	}

	public void setProduct_MadeIn(String product_MadeIn) {
		this.product_MadeIn = product_MadeIn;
	}

	public String getProduct_Material() {
		return product_Material;
	}

	public void setProduct_Material(String product_Material) {
		this.product_Material = product_Material;
	}

	

	public String getProduct_Measure() {
		return product_Measure;
	}

	public void setProduct_Measure(String product_Measure) {
		this.product_Measure = product_Measure;
	}

	public String getCarryOver() {
		return CarryOver;
	}

	public void setCarryOver(String carryOver) {
		CarryOver = carryOver;
	}



	private String product_id;
    private String producer_id;
    private String type;
    private String season;
    private String product_name;
    private String description;
    private String category;
    private String product_detail;
    private String product_MadeIn;
    private String product_Material;
    private String product_Measure;
    private String url;
    private String supply_price;
    private String CarryOver;
   

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    private Items items;

}
