package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 *@Author: zhaogenchun
 *@Date: 2016年12月15日 15:54:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubPendingCateGroyImportDTO {
	private String supplierCategoryDicId;
	private String supplierCategory;
	private String supplierGender;
	private String categroyType;
	private String mappingState;
	private String hubCategoryNo;
	private String createTime;
	private String updateTime;
	private String updateUser;
	private String supplierId;
	private String supplierName;
	private String genderDicId;

}
