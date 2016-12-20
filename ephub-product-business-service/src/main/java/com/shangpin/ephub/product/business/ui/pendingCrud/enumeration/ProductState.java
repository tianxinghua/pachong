package com.shangpin.ephub.product.business.ui.pendingCrud.enumeration;

public enum ProductState {

	/**
	 * 图片状态
	 */
	PICTURE_STATE(0,"pictureState"),
	/**
	 * 货号状态
	 */
	SPU_MODEL_STATE(1,"spuModelState"),
	/**
	 * 品类状态
	 */
	CATGORY_STATE(2,"catgoryState"),
	/**
	 * 品牌状态
	 */
	SPU_BRAND_STATE(3,"spuBrandState"),
	/**
	 * 性别状态
	 */
	SPU_GENDER_STATE(4,"spuGenderState"),
	/**
	 * 上市季节状态
	 */
	SPU_SEASON_STATE(5,"spuSeasonState"),
	/**
	 * 颜色状态
	 */
	SPU_COLOR_STATE(6,"spuColorState"),
	/**
	 * 材质状态
	 */
	MATERIAL_STATE(7,"materialState"),
	/**
	 * 产地状态
	 */
	ORIGIN_STATE(8,"originState"),
	/**
	 * 尺码状态
	 */
	SIZE_STATE(9,"sizeState");
	
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
