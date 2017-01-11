package com.shangpin.ephub.product.business.ui.pending.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 * 返回的货号信息
 */
@Getter
@Setter
public class SpuModelMsgVO implements Serializable {

   /**
	 * 
	 */
	private static final long serialVersionUID = 8548826208356011450L;

private Integer total;//总数

   private List<SpuModelVO> spuModels;
}
