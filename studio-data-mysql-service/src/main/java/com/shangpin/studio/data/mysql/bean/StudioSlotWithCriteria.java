package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.Studio;
import com.shangpin.studio.data.mysql.po.StudioCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlot;
import com.shangpin.studio.data.mysql.po.StudioSlotCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlot studioSlot;
	
	private StudioSlotCriteria criteria;
}
