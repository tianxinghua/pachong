package com.shangpin.iog.syn.sku.pending.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;
import lombok.*;

import java.io.Serializable;

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
