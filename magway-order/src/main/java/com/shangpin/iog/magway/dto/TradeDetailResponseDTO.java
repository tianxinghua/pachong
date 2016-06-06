package com.shangpin.iog.magway.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeDetailResponseDTO {

	TradeResponse Data;
	String Status;
	String Message;
	Errors Errors;
}
