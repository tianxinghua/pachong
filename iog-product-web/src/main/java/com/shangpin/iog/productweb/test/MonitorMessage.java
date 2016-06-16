package com.shangpin.iog.productweb.test;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MonitorMessage {
	
	private Map<String,String> supplierIdAndStatus;
	
}
