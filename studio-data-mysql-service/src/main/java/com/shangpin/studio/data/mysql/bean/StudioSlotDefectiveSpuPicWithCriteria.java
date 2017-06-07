package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPic;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPicCriteria;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotDefectiveSpuPicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlotDefectiveSpuPic StudioSlotDefectiveSpuPic;
	
	private StudioSlotDefectiveSpuPicCriteria criteria;
}
