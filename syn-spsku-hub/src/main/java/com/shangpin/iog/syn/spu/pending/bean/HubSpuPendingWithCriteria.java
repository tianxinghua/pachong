package com.shangpin.iog.syn.spu.pending.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.iog.syn.spu.pending.po.HubSpuPending;
import com.shangpin.iog.syn.spu.pending.po.HubSpuPendingCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuPendingWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -86223563656902875L;

	private HubSpuPending hubSpuPending;
	
	private HubSpuPendingCriteria criteria;
}
