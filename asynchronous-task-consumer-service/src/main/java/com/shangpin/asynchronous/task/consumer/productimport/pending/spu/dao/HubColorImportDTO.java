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
public class HubColorImportDTO {
	private String colorDicItemId;
	private String colorItemName;
	private String colorDicId;
	private String createTime;
	private String updateTime;
	private String updateUser;
}
