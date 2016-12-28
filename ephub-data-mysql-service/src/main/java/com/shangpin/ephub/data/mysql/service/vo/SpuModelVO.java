package com.shangpin.ephub.product.business.ui.pending.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
