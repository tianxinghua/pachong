package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlot;
import com.shangpin.studio.data.mysql.po.StudioSlotCriteria;
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
public class StudioSlotSpuSendDetailWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlotSpuSendDetail studioSlotSpuSendDetail;
	
	private StudioSlotSpuSendDetailCriteria criteria;
}
