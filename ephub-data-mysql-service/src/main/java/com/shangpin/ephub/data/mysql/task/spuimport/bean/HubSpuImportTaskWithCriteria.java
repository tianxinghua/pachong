package com.shangpin.ephub.data.mysql.task.spuimport.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTask;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTaskCriteria;

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
public class HubSpuImportTaskWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1951026872362200080L;

	private HubSpuImportTask hubSpuImportTask;
	
	private HubSpuImportTaskCriteria criteria;
}
