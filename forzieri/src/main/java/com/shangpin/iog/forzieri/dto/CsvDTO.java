package com.shangpin.iog.forzieri.dto;

public class CsvDTO {
	private String product_id;
	private String sku;
	private String brand_name;
	private String product_name;
	private String description;
	private String category_ids;
	private String cost_price;
	private String list_price;
	private String selling_price;
	private String quantity;
	private String ships_in_days;
	private String preorder;
	private String color;
	private String size;
	private String gender;
	private String material;
	private String weight;
	private String manufacturer_id;
	private String manufacturer_barcode;
	private String vistaImagel0;
	private String vistaImagel1;
	private String vistaImagel2;
	private String vistaImagel3;
	private String vistaImagel4;
	private String vistaImagel5;
	private String product_url;
	private String product_detail;
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
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
	public String getCategory_ids() {
		return category_ids;
	}
	public void setCategory_ids(String category_ids) {
		this.category_ids = category_ids;
	}
	public String getCost_price() {
		return cost_price;
	}
	public void setCost_price(String cost_price) {
		this.cost_price = cost_price;
	}
	public String getList_price() {
		return list_price;
	}
	public void setList_price(String list_price) {
		this.list_price = list_price;
	}
	public String getSelling_price() {
		return selling_price;
	}
	public void setSelling_price(String selling_price) {
		this.selling_price = selling_price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getShips_in_days() {
		return ships_in_days;
	}
	public void setShips_in_days(String ships_in_days) {
		this.ships_in_days = ships_in_days;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getManufacturer_id() {
		return manufacturer_id;
	}
	public void setManufacturer_id(String manufacturer_id) {
		this.manufacturer_id = manufacturer_id;
	}
	public String getManufacturer_barcode() {
		return manufacturer_barcode;
	}
	public void setManufacturer_barcode(String manufacturer_barcode) {
		this.manufacturer_barcode = manufacturer_barcode;
	}
	public String getVistaImagel0() {
		return vistaImagel0;
	}
	public void setVistaImagel0(String vistaImagel0) {
		this.vistaImagel0 = vistaImagel0;
	}
	public String getVistaImagel1() {
		return vistaImagel1;
	}
	public void setVistaImagel1(String vistaImagel1) {
		this.vistaImagel1 = vistaImagel1;
	}
	public String getVistaImagel2() {
		return vistaImagel2;
	}
	public void setVistaImagel2(String vistaImagel2) {
		this.vistaImagel2 = vistaImagel2;
	}
	public String getVistaImagel3() {
		return vistaImagel3;
	}
	public void setVistaImagel3(String vistaImagel3) {
		this.vistaImagel3 = vistaImagel3;
	}
	public String getVistaImagel4() {
		return vistaImagel4;
	}
	public void setVistaImagel4(String vistaImagel4) {
		this.vistaImagel4 = vistaImagel4;
	}
	public String getVistaImagel5() {
		return vistaImagel5;
	}
	public void setVistaImagel5(String vistaImagel5) {
		this.vistaImagel5 = vistaImagel5;
	}
	public String getProduct_url() {
		return product_url;
	}
	public void setProduct_url(String product_url) {
		this.product_url = product_url;
	}
	public String getProduct_detail() {
		return product_detail;
	}
	public void setProduct_detail(String product_detail) {
		this.product_detail = product_detail;
	}
	
	public String getPreorder() {
		return preorder;
	}
	public void setPreorder(String preorder) {
		this.preorder = preorder;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsvDTO other = (CsvDTO) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}
	
}
