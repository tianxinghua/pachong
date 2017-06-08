package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpu;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuCriteria;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotDefectiveSpuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlotDefectiveSpu studioSlotDefectiveSpu;
	
	private StudioSlotDefectiveSpuCriteria criteria;
}
