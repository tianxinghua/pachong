package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotDefectiveSpuCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7255745601518578512L;

	private StudioSlotDefectiveSpuCriteria criteria;
	
	private RowBounds rowBounds;
}
