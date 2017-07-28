package com.shangpin.asynchronous.task.consumer.productimport.common.enumeration;
/**
 * <p>Title: TaskState</p>
 * <p>Description:  </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月30日 下午4:06:47
 *
 */
public interface TaskState {

	public static final String SIGN = "；";
	public static final String FAIL = "校验失败";
	public static final String SUCCESS = "校验成功";
	public static final String MODEL_FAIL = "货号校验不通过";
	public static final String CATGORY_FAIL = "品类校验不通过";
	public static final String BRAND_FAIL = "品牌校验不通过";
	
}
