package com.shangpin.ephub.data.mysql.spu.pendingnohand.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReason;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReasonCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuPendingNohandleReasonWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -86223563656902875L;

	private HubSpuPendingNohandleReason hubSpuPendingNohandleReason;
	
	private HubSpuPendingNohandleReasonCriteria criteria;
}
