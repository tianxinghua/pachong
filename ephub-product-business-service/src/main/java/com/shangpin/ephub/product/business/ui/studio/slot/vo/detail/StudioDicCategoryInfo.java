package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioDicCategoryInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Long studioDicCategoryId;
    private String categoryFirst;
    private String categorySecond;
    private String studioName;
    private String updateUser;
    private Date updateTime;
}