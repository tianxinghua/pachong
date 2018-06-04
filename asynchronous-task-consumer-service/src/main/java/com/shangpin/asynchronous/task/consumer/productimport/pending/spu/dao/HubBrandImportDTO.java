package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 *@Author:
 *@Date: 2016年12月15日 15:54:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubBrandImportDTO {
	private String supplierBrandDicId;
	private String supplierId;
	private String supplierBrand;
	private String hubBrandNo;
	private String createTime;
	private String updateUser;
	private String updateTime;
}
