package com.shangpin.ephub.client.util;

/**
 * 导入、导出excel文件头模板
 * @author zhaogenchun
 * @date 2016年12月26日 下午12:35:52
 */
public class TaskImportTemplate {
	
	 public static String [] getPendingSkuTemplate(){
		String[] headers = {"供应商门户编号*","供应商编号", "供应商名称","供应商SpuNo", "品类名称","品类编号*", "品牌编号*","品牌名称","货号*","上市年份*","上市季节*","适应性别*",
					"供应商SkuNo*","商品名称*","条形码*",	"颜色*","规格类型","原尺码类型","原尺码值","材质*","产地*","进货价","币种","市场价","市场价币种","尺寸",
					"描述"};
		 return headers;
	 }
	 public static String[] getPendingSkuValueTemplate() {
			String [] temp = {"supplierId","supplierNo","supplierName","supplierSpuNo","hubCategoryName","hubCategoryNo","hubBrandNo","hubBrandName","spuModel","seasonYear","seasonName","hubGender",
					"supplierSkuNo","skuName","supplierBarcode","hubColor","specification","originalProductSizeType","originalProductSizeValue","hubMaterial","hubOrigin","supplyPrice","supplyPriceCurrency",
					"marketPrice","marketPriceCurrencyorg","hubSkuSize","spuDesc"};
			return temp;
	}
	 
	 public static String [] getPendingSpuTemplate(){
		 String[] headers = {"图片","供应商门户编号*","供应商编号", "供应商名称","供应商SpuNo", "品类名称","品类编号*", "品牌编号*","品牌名称","货号*","上市年份*","上市季节*","适应性别*",
				 "商品名称*","颜色*","材质*","产地*","描述","不符合项","商品状态"};
		 return headers;
	 }
	 public static String[] getPendingSpuValueTemplate() {
			String [] temp = {"spPicUrl","supplierId","supplierNo","supplierName","supplierSpuNo","hubCategoryName","hubCategoryNo","hubBrandNo","hubBrandName","spuModel","seasonYear","seasonName","hubGender",
					"spuName","hubColor","hubMaterial","hubOrigin","spuDesc","memo","spuState"};
			return temp;
		}
	 
	 public static String [] getHubProductTemplate(){
		 String[] headers = {"品类名称","品类编号*" ,"品牌编号*","品牌名称","货号*","上市年份*","上市季节*","适应性别*",
					"颜色*","原尺码类型","原尺码值","材质*","产地*"};
		 return headers;
	 }
	 public static String [] getHubProductValueTemplate(){
		 String[] headers = { "categoryName", "categoryNo","brandNo","brandName","spuModel","marketTime","season","gender",
					"hubColor","sizeType","skuSize","material","origin"};
		 return headers;
	 }
}
