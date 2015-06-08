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
 * <br/>2015年6月4日
 */

@Getter
@Setter
@NoArgsConstructor
public class Attributes {
	String description;
	Map<String,String> values;
}
