package com.shangpin.iog.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockUpdateLimitDTO {
	
	private String supplierId;
	private String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private Date updateTime;
	private int limitNum;

}
