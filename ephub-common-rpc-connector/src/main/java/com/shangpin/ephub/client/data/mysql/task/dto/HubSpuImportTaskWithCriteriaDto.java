package com.shangpin.ephub.client.data.mysql.task.dto;

import java.io.Serializable;

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
public class HubSpuImportTaskWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1951026872362200080L;

	private HubSpuImportTaskDto hubSpuImportTask;
	
	private HubSpuImportTaskCriteriaDto criteria;
}
