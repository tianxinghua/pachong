package com.shangpin.asynchronous.task.consumer.productexport.type.commited.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: CommitedExcelDto</p>
 * <p>Description: 已提交页面导出excel实体 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月11日 下午2:24:50
 *
 */
@Getter
@Setter
public class CommitedExcelDto {

	private String spuModel;
	/**
	 * 品牌名称
	 */
	private String hubBrandName;
	/**
	 * 对应的尚品品类
	 */
	private String hubCategoryName;
	/**
	 * 名称
	 */
	private String supplierName;
	/**
     * 状态  0:未寄出 1：已加入发货单  2：已发货  3:不处理
     */
	private Integer state ;
	
	private String relationState;
	
	private String updateTime;
	
	private String studioName;
	
}
