package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.studio.data.mysql.po.StudioSlotCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrackCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotLogistictTrackCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7255745601518578512L;

	private StudioSlotLogistictTrackCriteria criteria;
	
	private RowBounds rowBounds;
}
