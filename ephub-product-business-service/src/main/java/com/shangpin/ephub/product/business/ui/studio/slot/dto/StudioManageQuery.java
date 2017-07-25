package com.shangpin.ephub.product.business.ui.studio.slot.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: OpenBoxQuery</p>
 * <p>Description: OpenBox页面查询参数 </p>
 * <p>Company: </p> 
 * @author wangchao
 * @date 2017年7月24日 下午2:03:07
 *
 */
@Getter
@Setter
public class StudioManageQuery {
	private Long studioId;

	private String studioName;
    private String studioNo;
    private Byte studioType;
    private Byte studioStatus;
    private String studioContacts;
    private String contactInfo;
    private String telephone;
    private String email;
    private Integer period;
    private String country;
    private String address;
    private String memo;
    private String supplierNo;
    private String supplierId;
    private Short timeLag;
    private String createUser;
    private String updateUser;
    
    //扩展 摄影棚批次基础
    private Long studioDicSlotId;
    private Integer slotNumber;
    private Integer slotMinNumber;
    private Integer slotEfficiency;
    //扩展 品类摄影棚关系
    private Long studioDicCategoryId;
    private String categoryFirst;
    private String categorySecond;
    
    private Integer pageNo;
    private Integer pageSize;
    

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;
	
}
