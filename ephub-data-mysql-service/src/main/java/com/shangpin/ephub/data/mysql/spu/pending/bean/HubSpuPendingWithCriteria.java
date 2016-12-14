package com.shangpin.ephub.data.mysql.spu.pending.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
