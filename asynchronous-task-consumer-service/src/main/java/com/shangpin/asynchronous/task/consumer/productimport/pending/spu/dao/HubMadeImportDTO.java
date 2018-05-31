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
public class HubMadeImportDTO {
	private String hubSupplierValMappingId;
	private String supplierVal;
	private String hubVal;
	private String createTime;
	private String updateUser;
	private String updateTime;
}
