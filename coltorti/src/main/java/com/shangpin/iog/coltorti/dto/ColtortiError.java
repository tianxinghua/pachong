/**
 * 
 */
package com.shangpin.iog.coltorti.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误信息如：<br/>
 * <code>
 * {
  "message": "validation has failed",
  "errors": [
    {
      "message": "email field does not contain a valid email address"
    },
    {
      "message": "A is greater than B"
    }
  ]
}</code>
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */
@Setter
@Getter
public class ColtortiError implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 107874728206901221L;
	String message;
	List<ColtortiError> errors;
}
