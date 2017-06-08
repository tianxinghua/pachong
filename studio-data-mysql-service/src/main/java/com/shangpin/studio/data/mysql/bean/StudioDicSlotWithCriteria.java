package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.studio.data.mysql.po.dic.StudioDicSlot;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSlotCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioDicSlotWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioDicSlot studioDicSlot;
	
	private StudioDicSlotCriteria criteria;
}
