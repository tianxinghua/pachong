package com.shangpin.ephub.product.business.task.spuimport.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubTaskProductResponseDTO {

	private String taskNo;
	private String localFileName;
	private String taskState;
	private String createTime;
	private String createUser;
	private String updateUser;
	private String updateTime;
	private String resultFilePath;
}
