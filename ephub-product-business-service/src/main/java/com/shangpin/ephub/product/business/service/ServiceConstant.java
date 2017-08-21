package com.shangpin.ephub.product.business.service;

public interface ServiceConstant {
	
	/**
	 *
	 */
	public static final String  HUB_SEND_TO_SCM_EXIST="已存在，不需处理";


	public static final String  HUB_SEND_TO_SCM_EXIST_SCM_ERROR="SupplierSkuNo is exist!";

	/**
	 * 需要拍摄的供货商 SUPPLIERVALPARENTNO 代表 在hub_supplier_value_mapping表 hub_val_type 状态为5  的 supplier_val_parent_no列
	 */
	public static final String HUB_SLOT_NEED_SHOOT_SUPPLIERVALPARENTNO="1";
	/**
	 * 需要拍摄但不需要发货的供货商 SUPPLIERVALNO 代表 在hub_supplier_value_mapping表 hub_val_type 状态为5  的 supplier_val_no列
	 */
	public static final String HUB_SLOT_NOT_NEED_SEND_SUPPLIERVALNO="1";

	/**
	 * 使用摄影棚的图片备注
	 */
	public static final String  HUB_SPU_PIC_USE_SHOOT="USE SHOOT PIC";


}
