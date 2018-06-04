package com.shangpin.ephub.client.util;

/**
 * 导入、导出excel文件头模板   ,顺序随意，但headers和value要一一对应，value是数据库字段映射的Bean对象
 * 
 * @author zhaogenchun
 * @date 2016年12月26日 下午12:35:52
 */
public class TaskImportTemplate {

	/**
	 * 待处理sku导入导出模板
	 */
	public static String[] getPendingSkuTemplate() {
		String[] headers = { "供应商门户编号*", "供应商编号", "供应商名称", "供应商SpuNo*", "品类名称", "品类编号*", "品牌编号*", "品牌名称", "货号*",
				"上市年份*", "上市季节*", "适应性别*", "供应商SkuNo*", "商品名称*", "条形码*", "颜色*", "规格类型", "供应商尺码", "原尺码类型", "原尺码值", "材质*", "产地*",
				"进货价", "币种", "市场价", "市场价币种", "尺寸", "描述" };
		return headers;
	}
	public static String[] getPendingSkuValueTemplate() {
		String[] value = { "supplierId", "supplierNo", "supplierName", "supplierSpuNo", "hubCategoryName",
				"hubCategoryNo", "hubBrandNo", "hubBrandName", "spuModel", "seasonYear", "seasonName", "hubGender",
				"supplierSkuNo", "skuName", "supplierBarcode", "hubColor", "specification", "supplierSkuSize", "hubSkuSizeType",
				"hubSkuSize", "hubMaterial", "hubOrigin", "supplyPrice", "supplyPriceCurrency", "marketPrice",
				"marketPriceCurrencyorg", "originalProductSizeValue", "spuDesc" };
		return value;
	}

	
	/**
	 * 待处理spu导入导出模板
	 * @return
	 */
	public static String[] getPendingSpuTemplate() {
		String[] headers = { "图片", "详情链接", "供应商门户编号*", "供应商编号", "供应商名称", "供应商SpuNo", "供应商品类", "品类名称", "品类编号*", "供应商品牌","品牌编号*",
				"品牌名称", "供应商货号", "货号*", "上市年份*", "上市季节*", "适应性别*", "商品名称*", "供应商颜色", "颜色*", "供应商材质", "材质*", "产地*", "描述", "不符合项", "商品状态", "规格类型",
				"库存总数", "操作人", "过滤", "无法处理原因1", "无法处理原因2", "无法处理原因3", "无法处理原因4","重新拉图片" };
		return headers;
	}

	public static String[] getPendingSpuValueTemplate() {
		String[] value = { "spPicUrl", "productInfoUrl", "supplierId", "supplierNo", "supplierName", "supplierSpuNo",
				"supplierCategoryname", "hubCategoryName", "hubCategoryNo", "supplierBrandName", "hubBrandNo", "hubBrandName", "supplierSpuModel", "spuModel",
				"seasonYear", "seasonName", "hubGender", "spuName", "supplierSpuColor", "hubColor", "supplierMaterial", "hubMaterial", "hubOrigin", "spuDesc",
				"memo", "spuState", "specificationType", "totalStock", "updateUser", "filter", "reason1", "reason2",
				"reason3", "reason4","picRetry" };
		return value;
	}

	
	/**
	 * 全部商品页，商品导出模板
	 * 
	 * @return
	 */
	public static String[] getProductAllTemplate() {
		String[] headers = { "供应商门户编号*", "供应商编号", "供应商名称", "供应商SpuNo*", "品类名称", "品类编号*", "品牌编号*", "品牌名称", "货号*", "商品状态",
				"上市年份*", "上市季节*", "供应商季节", "适应性别*", "供应商SkuNo*", "尚品sku编号", "商品名称*", "条形码*", "颜色*", "规格类型", "原尺码类型",
				"原尺码值", "库存", "材质*", "产地*", "进货价", "币种", "市场价", "市场价币种", "尺寸", "描述", "供应商图片URL", "最后拉取时间" };
		return headers;
	}
	public static String[] getProductAllValueTemplate() {
		String[] value = { "supplierId", "supplierNo", "supplierName", "supplierSpuNo", "hubCategoryName",
				"hubCategoryNo", "hubBrandNo", "hubBrandName", "spuModel", "spuState", "seasonYear", "seasonName",
				"supplierSeasonName", "hubGender", "supplierSkuNo", "spSkuNo", "skuName", "supplierBarcode", "hubColor",
				"specification", "hubSkuSizeType", "hubSkuSize", "stock", "hubMaterial", "hubOrigin", "supplyPrice",
				"supplyPriceCurrency", "marketPrice", "marketPriceCurrencyorg", "originalProductSizeValue", "spuDesc",
				"supplierUrl", "lastPullTime" };
		return value;
	}

	
	/**
	 * studio批次导出模板
	 * 
	 * @return
	 */
	public static String[] getStudioSlotTemplate() {
		String[] headers = { "日期", "摄影棚名称", "分配批次号", "批次状态", "申请状态", "申请方", "申请时间", "到货状态", "到货日期", "拍摄状态", "计划拍摄日期",
				"拍摄日期", "状态更新日期" };
		return headers;
	}
	/**
	 * 字典颜色导出模板
	 *
	 */


}
