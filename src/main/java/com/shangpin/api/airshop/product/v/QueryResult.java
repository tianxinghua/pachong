package com.shangpin.api.airshop.product.v;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class QueryResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5084606941196543686L;
	
	private HttpStatus httpStatus;
	
	private List<SkuVO> skus;

}
