package com.shangpin.ephub.product.business.common.enumeration;

public interface GlobalConstant {
	
	/**
	 * 用于redis缓存供应商信息的key
	 */
	public static final String  REDIS_ORDER_SUPPLIER_KEY="REDIS_ORDER_SUPPLIER_KEY";
	/**
	 * 用于redis缓存品类信息的key
	 */
	public static final String REDIS_HUB_CATEGORY_KEY="REDIS_HUB_CATEGORY_KEY";
	/**
	 * 用于redis缓存品牌信息的key
	 */
	public static final String REDIS_HUB_BRAND_KEY="REDIS_HUB_BRAND_KEY";
	/**
	 * 用于redis缓存尺码信息的key
	 */
	public static final String REDIS_HUB_SIZE_KEY = "REDIS_HUB_SIZE_KEY";
	/**
	 * 尺存的标记
	 *
	 * /**
	 *          *         /// 尺码
	 *          *         /// </summary>
	 *          *         CM=1,
	 *          *         /// <summary>
	 *          *         /// 样式尺寸
	 *          *         /// </summary>
	 *          *         YS=2,
	 *          *         /// <summary>
	 *          *         /// 容量
	 *          *         /// </summary>
	 *          *         RL=3,
	 *          *         /// <summary>
	 *          *         /// 版本
	 *          *         /// </summary>
	 *          *         BB=4,
	 *          *         /// <summary>
	 *          *         /// 重量
	 *          *         /// </summary>
	 *          *         ZL=5,
	 *          *         /// <summary>
	 *          *         /// 克重
	 *          *         /// </summary>
	 *          *         KZ = 6,
	 *          *         /// <summary>
	 *          *         /// 香调
	 *          *         /// </summary>
	 *          *         XD = 7,
	 *          *         /// <summary>
	 *          *         /// 面值
	 *          *         /// </summary>
	 *          *         MZ = 8,
	 *
	 *
	 */
	public static final String REDIS_HUB_MEASURE_SIGN_KEY = "尺寸";

	public static final String HUB_SKE_SIZE_TYPE_EXCLUDE="排除";

	public static final String SCM_SIZE_TYPE_CM="尺码";

	public static final String SCM_SIZE_TYPE_YSCC="尺寸";

	public static final String SCM_SIZE_TYPE_RL="容量";

	public static final String SCM_SIZE_TYPE_BB="版本";

	public static final String SCM_SIZE_TYPE_ZL="重量";


	public static final String SCM_SIZE_TYPE_KZ="克重";

	public static final String SCM_SIZE_TYPE_XD="香调";

	public static final String SCM_SIZE_TYPE_MZ="面值";


	/**
	 * 用于redis缓存代购直发
	 */
	public static final String REDIS_SUPPLIER_HOTBOOM_DIRECT = "REDIS_SUPPLIER_HOTBOOM_DIRECT";

}
