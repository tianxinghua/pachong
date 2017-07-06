package com.shangpin.ephub.product.business.ui.studio.studio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/22.
 */
@Getter
@Setter
@ToString
public class StudioSlotQueryDto implements Serializable {
    private static final long serialVersionUID = 2934640037904185730L;
    private Long studioSlotId;
    private String studioSlotIds;
    private Long studioId;
    private String studioName;
    private String studioNo;
    private String categoryNos;

    private Long supplierId;
    private String supplierUser;
    private String startTime;
    private String endTime;
    private int pageIndex;
    private int pageSize;

}
