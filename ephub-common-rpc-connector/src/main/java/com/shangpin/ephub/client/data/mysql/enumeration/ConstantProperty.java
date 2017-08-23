package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * Created by loyalty on 16/12/14.
 */
public class ConstantProperty {
	   public  static String PENDING_PRODUCT_MAP_KEY ="PENDING_PRODUCT_MAP_KEY";

	   public  static String DATA_CREATE_USER = "pendingService";

	   /**
	    * 品牌的分页最大查询数量
	    */
	   public  static  int MAX_BRANDK_MAPPING_QUERY_NUM=20000;

	   /**
	    * 通用查询数量
	    */
	   public static int MAX_COMMON_QUERY_NUM= 10000;

	   /**
	    * 颜色对照表查询每页最大数量
	    */
	   public static int MAX_COLOR_ITEM_QUERY_NUM = 20000;

	   /**
	    * 材质对照表每页最大查询数量
	    */
	   public static int MAX_MATERIAL_QUERY_NUM = 20000;

	   // --------------品类映射
	   public static String REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY="REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY="REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY";
	   public static int  REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME = 600;
	    // -------------品类映射end

	   // --------------尺码映射
	   public static String SIZE_TYPE="尺寸";
	   public static String REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY="REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY";
	   public static String REDIS_EPHUB_SUPPLIER_ALL_SIZE_MAPPING_KEY="ALL";
	   public static String REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY="REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY";
	   public static int  REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME = 60;
	   // --------------尺码映射end
	   
	   // --------------颜色映射-------
	   public static String REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY = "REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY";
	   // --------------颜色 映射end-------

	   // --------------材质映射------------------
	   //第一级别整句映射
	   public static String REDIS_EPHUB_FIRST_MATERIAL_MAPPING_MAP_KEY = "REDIS_EPHUB_FIRST_MATERIAL_MAPPING_MAP_KEY";
	   //第二级别词语映射
	   public static String REDIS_EPHUB_SECOND_MATERIAL_MAPPING_MAP_KEY = "REDIS_EPHUB_SECOND_MATERIAL_MAPPING_MAP_KEY";
	   //第三级别单词映射
	   public static String REDIS_EPHUB_THREE_MATERIAL_MAPPING_MAP_KEY = "REDIS_EPHUB_THREE_MATERIAL_MAPPING_MAP_KEY";
	   //第三级别单词映射
	   public static String REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY = "REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY";
	   // --------------材质映射end------------------
	   
	   // --------------品牌映射-------
	   public static String REDIS_EPHUB_HUB_BRAND_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_BRAND_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY = "REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY";
	   // --------------品牌映射end-------
	   
	   // --------------产地映射-------
	   public static String REDIS_EPHUB_HUB_ORIGIN_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_ORIGIN_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY = "REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY";
	   // --------------产地映射end-------
	   
	   // --------------性别映射-------
	   public static String REDIS_EPHUB_HUB_GENDER_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_GENDER_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_SUPPLIER_GENDER_MAPPING_MAP_KEY = "REDIS_EPHUB_SUPPLIER_GENDER_MAPPING_MAP_KEY";
	   // --------------性别映射end-------
	   
	   // --------------季节映射-------
	   public static String REDIS_EPHUB_HUB_SEASON_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_SEASON_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY = "REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY";
	   // --------------季节映射end-------
	   
	   // --------------货号映射-------
	   public static String REDIS_EPHUB_HUB_BRAND_MODEL_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_BRAND_MODEL_MAPPING_MAP_KEY";
	   public static String REDIS_EPHUB_HUB_BRAND_CATEGORY_MODEL_MAPPING_MAP_KEY = "REDIS_EPHUB_HUB_BRAND_CATEGORY_MODEL_MAPPING_MAP_KEY";
	   // --------------货号映射end-------
	   
	   //儿童品类品牌映射
	   public static String REDIS_EPHUB_KID_BRAND_MAPPING_MAP_KEY = "REDIS_EPHUB_KID_BRAND_MAPPING_MAP_KEY";
	   
	   //品类下保留的所有的品牌
	   public static String REDIS_EPHUB_CATEGORY_BRNAD_MAPPING_MAP_KEY="REDIS_EPHUB_CATEGORY_BRAND_MAPPING_MAP_KEY";
	}
