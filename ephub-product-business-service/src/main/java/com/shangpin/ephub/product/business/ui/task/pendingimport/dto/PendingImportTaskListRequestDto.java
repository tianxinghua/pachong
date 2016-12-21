package com.shangpin.ephub.product.business.ui.task.pendingimport.dto;

import java.io.Serializable;

import java.util.Date;

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
public class PendingImportTaskListRequestDto implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;
    /**
     * 原始文件名
     */
    private String localFileName;
    private byte taskState;
    private String startDate;
    private String endDate;
    private int pageIndex;
    private int pageSize;

}