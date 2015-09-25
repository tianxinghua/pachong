/**
 * 
 */
package com.shangpin.iog.coltorti.dto;

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
public class ColtortiStock {

	private String id;
	private String total;
	Map<String,Map<String,String>> scalars;
	String deposit_id;
}
