package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title: ErrorType</p>
 * <p>Description: hub_spu_pending_nohandle_reason.error_type </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月3日 下午3:15:03
 *
 */
public enum ErrorType {
	
	ITEM_CODE_ERROR((byte)1, "Item Code Error", "货号错误"),
	MATERIAL_INFO_ERROR((byte)2, "Material Info Error", "材质问题"),
	GENDER_INFO_ERROR((byte)3, "Gender Info Error", "性别问题"),
	ORIGIN_INFO_ERROR((byte)4, "Origin Info Error", "产地问题"),
	PHOTO_ERROR((byte)5, "Photo Error", "照片错误"),
	SUPPLIER_SKU_ERROR((byte)6, "Supplier SKU Error", "SKU问题"),
	PRICE_ERROR((byte)7, "Price Error", "价格错误"),
	SIZE_ISSUE((byte)8, "Size Issue", "尺码问题"),
	BRAND_SELECTION((byte)9, "Brand Selection", "品牌问题");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 英文描述信息
     */
    private String desEn;
    /**
     * 中文描述信息
     */
    private String desCh;
    
    ErrorType(byte index,String desEn,String desCh){
		this.index = index;
		this.setDesEn(desEn);
		this.setDesCh(desCh);
	}
    public byte getIndex() {
        return index;
    }
    public void setIndex(byte index) {
        this.index = index;
    }
	public String getDesEn() {
		return desEn;
	}
	public void setDesEn(String desEn) {
		this.desEn = desEn;
	}
	public String getDesCh() {
		return desCh;
	}
	public void setDesCh(String desCh) {
		this.desCh = desCh;
	}
    
}
