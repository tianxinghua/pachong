package com.shangpin.ephub.product.business.ui.pending.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 */
@Getter
@Setter
public class SpuPendingVO extends  SpuPendingCommonVO implements Serializable {




    /**
	 * 
	 */
	private static final long serialVersionUID = 8208612922999639039L;

	/**
     * 是否默认供货商
     */
    private Boolean isDefaultSupplier;

    /**
     * 供货商图片信息
     */

    private List<SpuPendingPicVO>  picVOs;



}
