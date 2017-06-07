package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.studio.data.mysql.po.StudioMatchSpu;
import com.shangpin.studio.data.mysql.po.StudioMatchSpuCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioMatchSpuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioMatchSpu studioMatchSpu;
	
	private StudioMatchSpuCriteria criteria;
}
