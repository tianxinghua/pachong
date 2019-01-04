package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import org.springframework.web.bind.annotation.PathVariable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TMSTrack implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String updateTimeBegin;
	private String updateTimeEnd;
	private String status;
	private String taskBatchNo;
	private String masterNo;
	
}
