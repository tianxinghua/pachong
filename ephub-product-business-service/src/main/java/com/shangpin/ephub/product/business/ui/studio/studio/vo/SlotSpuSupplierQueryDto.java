package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/20.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlotSpuSupplierQueryDto implements Serializable {
    private static final long serialVersionUID = -1607719936360294163L;

    private Integer pageIndex;
    private Integer pageSize;

    private String supplierId;
    /**
     * 供应商编号
     */
    private String supplierNo;
    /**
     * 状态查询
     */
    private Byte state;

    /**
     * 供货商商品编号
     */
    private String supplierSpuId;

    /**
     * 供货商商品编号
     */
    private String supplierSpuNo;
    /**
     * 货号
     */
    private String spuModel;

    private String slotSpuNo;

    private String brandName;

    private String categoryName;

    /**
     * 尚品季节名称
     */
    private String seasonName;

    private Date startTime;

    private Date endTime;

}
