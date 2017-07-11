package com.shangpin.asynchronous.task.consumer.productexport.template;

/**
 * <p>Title: Template</p>
 * <p>Description: 导出首行模板（第一行） </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月7日 下午2:14:32
 *
 */
public interface Template {

	/**
	 * 待拍照页面导出模板
	 */
	public static String[] WAIT_SHOOT_CH = {"SpuPendingId","供应商门户编号*","供应商编号","供应商名称","供应商品类","品类名称","品类编号*","品牌编号*","品牌名称","货号*"};
	public static String[] WAIT_SHOOT_EN = {"spuPendingId","supplierId","supplierNo","supplierName","supplierCategoryname","hubCategoryName","hubCategoryNo","hubBrandNo","hubBrandName","spuModel"};
	/**
	 * 已提交页面导出模板
	 */
	public static String[] COMMITED_CH = {"货号","品牌","品类","供应商","关系状态","摄影棚","更新时间"}; 
	public static String[] COMMITED_EN = {"spuModel","hubBrandName","hubCategoryName","supplierName","relationState","studioName","updateTime"};
}
