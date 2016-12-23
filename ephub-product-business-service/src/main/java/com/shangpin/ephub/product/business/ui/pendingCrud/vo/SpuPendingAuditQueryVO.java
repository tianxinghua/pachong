package com.shangpin.ephub.product.business.ui.pendingCrud.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 */
@Setter
@Getter
public class SpuPendingAuditQueryVO implements Serializable {

    private String brandNo;

    private String categroyNo;
    /**
     * 货号
     */
    private String spuModel;

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


}
