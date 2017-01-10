package com.shangpin.ephub.data.mysql.product.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 16/12/23.
 */
@Setter
@Getter
public class SpuPendingAuditDto extends SpuPendingDto {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7854412961944681630L;
	/**
     * 0 :信息待完善 1 : 待复核 2 :已处理(审核通过) 3：无法处理 4:过滤不处理
     */
    private Integer  auditStatus;





}
