package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * <p>Title: ErrorReason</p>
 * <p>Description: hub_spu_pending_nohandle_reason.error_reason </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月3日 下午3:15:42
 *
 */
public enum ErrorReason {
	
	NO_COLOR_CODE((byte)11, "No Color Code", "缺颜色码", ErrorType.ITEM_CODE_ERROR),
	NO_MATERIAL_CODE((byte)12, "No Material Code", "缺材质码", ErrorType.ITEM_CODE_ERROR),
	WRONG_CODE_RULE((byte)13, "Wrong Code Rule", "与品牌官网不符", ErrorType.ITEM_CODE_ERROR),
	WRONG_MATERIAL_COMPOSITION((byte)21, "Wrong Material Composition", "材质信息描述有误", ErrorType.MATERIAL_INFO_ERROR),
	WRONG_MATERIAL_PERCENTAGE((byte)22, "Wrong Material Percentage", "材质百分比错误", ErrorType.MATERIAL_INFO_ERROR),
	NO_MATERIAL_INFO((byte)23, "No Material Info", "没有材质", ErrorType.MATERIAL_INFO_ERROR),
	CHILD_ADULT_INVERSION((byte)31, "Child-Adult Inversion", "提供性别不符 儿童-成人 颠倒", ErrorType.GENDER_INFO_ERROR),
	MAN_WOMAN_INVERSION((byte)32, "Man-Woman Inversion", "男女颠倒", ErrorType.GENDER_INFO_ERROR),
	NO_ORIGIN_INFO((byte)41, "No Origin Info", "产地为空", ErrorType.ORIGIN_INFO_ERROR),
	WRONG_MAPPING_OF_CODE((byte)51, "Wrong Mapping of Code", "提供图片与实际货号不符", ErrorType.PHOTO_ERROR),
	No_Photo((byte)52, "No Photo", "无图片", ErrorType.PHOTO_ERROR),
	DIFFERENT_SPU_TO_SAME_ITEM_CODE((byte)61, "Different SPU to Same Item Code", "同款同货号提供多个SPU", ErrorType.SUPPLIER_SKU_ERROR),
	NO_MARKET_PRICE((byte)71, "No Market Price", "没有市场价", ErrorType.PRICE_ERROR),
	NO_SUPPLIER_PRICE((byte)72, "No Supplier Price", "没有供价", ErrorType.PRICE_ERROR),
	TOO_LARGE_OR_SMALL((byte)81, "Too Large / Small", "太大或太小", ErrorType.SIZE_ISSUE),
	WRONG_SIZE((byte)82, "Wrong Size", "错误尺码", ErrorType.SIZE_ISSUE),
	UNPROFITABLE_BRAND((byte)91, "Unprofitable Brand", "非运营品牌", ErrorType.BRAND_SELECTION);
	
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
    /**
     * 所属错误类型
     */
    private ErrorType errorType;
    
    ErrorReason(byte index,String desEn,String desCh,ErrorType errorType){
		this.index = index;
		this.desEn = desEn;
		this.desCh = desCh;
		this.errorType = errorType;
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
	public ErrorType getErrorType() {
		return errorType;
	}
	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}
}
