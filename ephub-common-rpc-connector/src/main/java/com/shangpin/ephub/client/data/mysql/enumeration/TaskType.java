package com.shangpin.ephub.client.data.mysql.enumeration;

public enum TaskType {

	/**
	 * 一般任务
	 */
	NO_TYPE(0,"NO_TYPE"),
	/**
	 * pending-spu导入
	 */
	PENDING_SPU(1,"PENDING_SPU"),
	/**
	 * pending-sku导入
	 */
	PENDING_SKU(2,"PENDING_SKU"),
	/**
	 * hub导入
	 */
	HUB_PRODUCT(3,"HUB_PRODUCT"),
	
	/**
	 * pending-spu导出
	 */
	EXPORT_PENDING_SPU(4,"pending-spu导出"),
	/**
	 * pending-sku导出
	 */
	EXPORT_PENDING_SKU(5,"pending-sku导出"),
	
	EXPORT_HUB_SELECTED(6,"hub供价导出"),
	EXPORT_HUB_PIC(7,"hub图片导出"),
	EXPORT_HUB_NOT_HANDLE_PIC(9,"hub未修图片导出"),
	EXPORT_HUB_CHECK_PIC(8,"hub勾选图片导出"),
	REFRESH_DIC(10,"刷新字典"),
	/**
	 * 全部商品页，商品导出
	 */
	ALL_PRODUCT(11,"全部商品页-商品导出"),
	
	EXPORT_SPU_ALL(12,"全部商品页-SPU导出"),
	
	EXPORT_SKU_ALL(13,"全部商品页-SKU导出"),
	
	EXPORT_SUTDIO_SLOT(14,"studio批次导出"),
	/**
	 * 待拍照导入
	 */
	IMPORT_SLOT_SPU(15,"待拍照导入"),
	
	EXPORT_WAIT_SHOOT(16,"待拍照导出"),
	
	EXPORT_COMMITED(17,"已提交导出"),
	AIRSHOP_IMPORT(18,"airshop导入"),
	SUPPLIER_PIC_EXPORT(19,"供应商图片导出"),

	IMPORT_COLOR(20,"图片导入"),
	EXPORT_COLOR(21,"图片导出"),

	IMPORT_MATERIAL(22,"材质导入"),
	EXPORT_MATERIAL(23,"材质导出"),

	IMPORT_ORIGIN(24,"产地导入"),
	EXPORT_ORIGIN(25,"产地导出"),

	IMPORT_CATEGORY(24,"品类导入"),
	EXPORT_CATEGORY(25,"品类导出"),

	IMPORT_BRAND(24,"品牌导入"),
	EXPORT_BRAND(25,"品牌导出"),

	;


	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    TaskType(Integer index,String description){
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
