package com.shangpin.ephub.data.mysql.spu.pendingnohand.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReasonCriteria;
import lombok.*;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuPendingNohandleReasonCriteriaWithRowBounds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3930645599632142208L;

	private HubSpuPendingNohandleReasonCriteria criteria;
	
	private RowBounds rowBounds;
}
