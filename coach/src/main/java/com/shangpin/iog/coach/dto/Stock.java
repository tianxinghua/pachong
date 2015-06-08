/**
 * 
 */
package com.shangpin.iog.coach.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月5日
 */
@Getter
@Setter
@NoArgsConstructor
public class Stock {

	private String id;
	private String total;
	Map<String,Map<String,String>> scalars;
	String deposit_id;
}
