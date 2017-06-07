package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMaster;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetailCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotReturnMasterWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlotReturnMaster studioSlotReturnMaster;
	
	private StudioSlotReturnMasterCriteria criteria;
}
