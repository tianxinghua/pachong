package com.shangpin.ephub.data.mysql.sku.pending.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;

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
public class HubSkuPendingWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private HubSkuPending hubSkuPending;
	
	private HubSkuPendingCriteria criteria;
}
