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
 * <br/>2015年6月4日
 */

@Getter
@Setter
@NoArgsConstructor
public class ColtortiAttributes {
	String description;
	Map<String,String> values;
}
