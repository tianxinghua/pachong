package com.shangpin.asynchronous.task.consumer.productexport.pending.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingProducts {

	private List<PendingProductDto> produts;
	private int total;
	private String createUser;
}
