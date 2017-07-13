package com.shangpin.ephub.client.data.studio.slot.slot.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: OpenBoxQuery</p>
 * <p>Description: OpenBox页面查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月9日 下午2:03:07
 *
 */
@Getter
@Setter
public class SlotManageQuery {

	private String date;
	private String studioNo;
	private String slotNo;
	private Byte slotStatus;
	private Byte applyStatus;
	private String applySupplierId;
	private String applyUser;
	private String applyTime;
	private String arriveTime;
	private Byte arriveStatus;
	private Byte shotStatus;
	private String planShootTime;
	private String shootTime;
	private String operatorName;
	private Integer pageNo;
	private Integer pageSize;
	//扩展
	private String supplierName;
	private String barCode;
	private Integer state;
	private String trackName;
	private String trackNo;
	private String masterId;
	private Integer quantity;
	private Integer actualNumber;
	private String sender;
	private byte milestone;
	private Date startDate;
	private Date endDate;
	private String studioId;
}
