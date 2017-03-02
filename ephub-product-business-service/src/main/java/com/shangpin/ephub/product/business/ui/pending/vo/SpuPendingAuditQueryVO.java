package com.shangpin.ephub.product.business.ui.pending.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 */
@Setter
@Getter
@ToString
public class SpuPendingAuditQueryVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6102475825182786546L;

	private String brandNo;

    private String categoryNo;
    /**
     * 货号
     */
    private String spuModel;

    //供货商编号
    private String supplierNo;


    /**
     * 更新的开始时间
     */
    private String startDate;
    /**
     * 更新的结束时间
     */
    private String  endDate;

    private Integer page; //页码

    private Integer pageSize; //每页显示数量

    /**
     *
     * 查询状态 内部使用
     */
    private Integer status;


}
