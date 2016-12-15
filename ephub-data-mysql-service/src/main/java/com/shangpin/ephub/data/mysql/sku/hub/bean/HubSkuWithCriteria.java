package com.shangpin.ephub.data.mysql.sku.hub.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSkuCriteria;

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
public class HubSkuWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSku hubSku;
	
	private HubSkuCriteria criteria;
}
