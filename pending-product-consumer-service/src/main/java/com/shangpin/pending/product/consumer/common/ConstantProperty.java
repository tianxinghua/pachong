package com.shangpin.pending.product.consumer.common;

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

   public static String REDIS_EPHUB_PENDING_CATEGORY_KEY="REDIS_EPHUB_PENDING_CATEGORY_KEY";


   public static String REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY="REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY";

   public static int  REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME = 60;

}
