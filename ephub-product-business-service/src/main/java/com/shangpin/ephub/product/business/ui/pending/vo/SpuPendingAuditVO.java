package com.shangpin.ephub.product.business.ui.pending.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 16/12/23.
 */
@Setter
@Getter
public class SpuPendingAuditVO  extends SpuPendingVO{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3008587403829352646L;
	/**
     * 0 :信息待完善 1 : 待复核 2 :已处理(审核通过) 3：无法处理 4:过滤不处理
     */
    private String  auditStatus;

    private String startDate;

    private String endDate;
	/**
	 * 单个更新 还是按查询条件多个更新
	 * 按查询条件:true 单个:false
	 */
	private boolean isMulti;





}
