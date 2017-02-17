package com.shangpin.iog.syn.sku.hub.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSkuCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSkuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSku hubSku;
	
	private HubSkuCriteria criteria;
}
