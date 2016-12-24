package com.shangpin.ephub.client.util;

import java.util.HashMap;
import java.util.Map;

public class TaskImportTemplate {
//	 private static Map<Integer,String> pendingSkuMap = new HashMap<Integer,String>();
//	 private static Map<Integer,String> pendingSpuMap = new HashMap<Integer,String>();
//	 private static Map<Integer,String> hubSkuMap = new HashMap<Integer,String>();
	 
	 public static String [] getPendingSkuTemplate(){
			String[] headers = { "供应商编号", "供应商名称", "品类名称", "品牌编号*","品牌名称","货号*","上市年份*","上市季节*","适应性别*","供应商SkuNo*","商品名称*","条形码*",
					"颜色*","规格类型","原尺码类型","原尺码值","材质*","产地*","进货价","币种",
					"市场价","市场价币种","尺寸","描述"};
//		 pendingSkuMap.put(0,"供应商编号");
//		 pendingSkuMap.put(0,"供应商名称");
//		 pendingSkuMap.put(0,"品类名称");
//		 pendingSkuMap.put(0,"品类编号*");
//		 pendingSkuMap.put(0,"品牌编号*");
//		 pendingSkuMap.put(0,"品牌名称");
//		 pendingSkuMap.put(0,"货号*");
//		 pendingSkuMap.put(0,"上市年份*");
//		 pendingSkuMap.put(0,"上市季节*");
//		 pendingSkuMap.put(0,"适应性别*");
//		 pendingSkuMap.put(0,"供应商SkuNo*");
//		 pendingSkuMap.put(0,"商品名称*");
//		 pendingSkuMap.put(0,"条形码*");
//		 pendingSkuMap.put(0,"颜色*");
//		 pendingSkuMap.put(0,"规格类型");
//		 pendingSkuMap.put(0,"原尺码类型");
//		 pendingSkuMap.put(0,"原尺码值");
//		 pendingSkuMap.put(0,"材质*");
//		 pendingSkuMap.put(0,"产地*");
		 return headers;
	 }
	 
	 public static String [] getPendingSpuTemplate(){
		 String[] headers = { "供应商编号", "供应商名称", "品类名称", "品牌编号*","品牌名称","货号*","上市年份*","上市季节*","适应性别*","商品名称*",
					"颜色*","材质*","产地*","描述"};
		 return headers;
	 }
	 
	 public static String [] getHubProductTemplate(){
		 String[] headers = { "供应商编号", "供应商名称", "品类名称", "品牌编号*","品牌名称","货号*","适应性别*",
					"颜色*","原尺码类型","原尺码值","材质*","产地*","市场价","市场价币种"};
		 return headers;
	 }
	 
}
