package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioDicSlotInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Long studioDicSlotId;
    private Integer slotNumber;
    private Integer slotMinNumber;
    private Integer slotEfficiency;
    private Date updateTime;
    private String updateUser;
    private String studioName;
}