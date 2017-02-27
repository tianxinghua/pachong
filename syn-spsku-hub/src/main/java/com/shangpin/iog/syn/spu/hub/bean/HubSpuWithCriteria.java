package com.shangpin.iog.syn.spu.hub.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.iog.syn.spu.hub.po.HubSpu;
import com.shangpin.iog.syn.spu.hub.po.HubSpuCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -260326594133473196L;

	private HubSpu hubSpu;
	
	private HubSpuCriteria criteria;
}
