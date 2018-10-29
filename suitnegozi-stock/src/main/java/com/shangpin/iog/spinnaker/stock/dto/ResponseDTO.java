package com.shangpin.iog.spinnaker.stock.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ResponseDTO {

	private String TotalSku;
	private String TotalQty;
	private List<Stock> listStockData;
}
