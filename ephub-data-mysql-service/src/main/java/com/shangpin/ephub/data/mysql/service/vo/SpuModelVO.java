package com.shangpin.ephub.data.mysql.service.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 * 货号列表
 */
@Getter
@Setter
public class SpuModelVO implements Serializable {

   /**
	 * 
	 */
	private static final long serialVersionUID = 5147199192561457186L;

private String spuModel;//货号

   private String brandNo;
}
