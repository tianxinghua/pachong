package com.shangpin.ephub.data.mysql.spu.hub.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;

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
public class HubSpuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -260326594133473196L;

	private HubSpu hubSpu;
	
	private HubSpuCriteria criteria;
}
