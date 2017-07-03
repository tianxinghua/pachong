package com.shangpin.asynchronous.task.consumer.productimport.common.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: CheckResultDto</p>
 * <p>Description: 待处理（待拍照）导入的数据校验结果 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月30日 下午3:27:35
 *
 */
@Getter
@Setter
public class CheckResultDto {

	/**
	 * 任务编号
	 */
	private String taskNo;
	/**
	 * 货号
	 */
	private String spuModel;
	/**
	 * 该条数据检验的结果（成功/失败）
	 */
	private String taskState;
	/**
	 * 处理意见
	 */
	private String processInfo;
	/**
	 * 
	 */
	private String spuNewModel;
}
