package com.shangpin.iog.syn.spu.hub.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.iog.syn.spu.hub.po.HubSpuCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuCriteriaWithRowBounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -216129288971772698L;

	private HubSpuCriteria criteria;
	
	private RowBounds rowBounds;
}
