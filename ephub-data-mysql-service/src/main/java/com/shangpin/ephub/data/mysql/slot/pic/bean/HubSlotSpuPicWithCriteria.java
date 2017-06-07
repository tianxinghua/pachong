package com.shangpin.ephub.data.mysql.slot.pic.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPic;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPicCriteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSlotSpuPicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSlotSpuPic hubSlotSpuPic;
	
	private HubSlotSpuPicCriteria criteria;
}
