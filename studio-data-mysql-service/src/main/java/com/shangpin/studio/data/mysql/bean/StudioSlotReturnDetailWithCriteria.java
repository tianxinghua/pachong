package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetailCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMaster;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotReturnDetailWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlotReturnDetail studioSlotReturnDetail;
	
	private StudioSlotReturnDetailCriteria criteria;
}
