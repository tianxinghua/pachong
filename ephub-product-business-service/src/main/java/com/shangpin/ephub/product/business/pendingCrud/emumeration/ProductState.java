package com.shangpin.ephub.product.business.pendingCrud.emumeration;

public enum ProductState {

	
	pictureState(0,"pictureState"),
	spuModelState(1,"spuModelState"),
	catgoryState(2,"catgoryState"),
	spuBrandState(3,"spuBrandState"),
	spuGenderState(4,"spuGenderState"),
	spuSeasonState(5,"spuSeasonState"),
	spuColorState(6,"spuColorState");
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    ProductState(Integer index,String description){
		this.index = index;
		this.description = description;
	}
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
