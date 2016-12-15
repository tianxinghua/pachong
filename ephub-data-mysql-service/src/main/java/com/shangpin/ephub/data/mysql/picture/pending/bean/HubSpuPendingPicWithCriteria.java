package com.shangpin.ephub.data.mysql.picture.pending.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPic;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPicCriteria;

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
public class HubSpuPendingPicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8098281033321763241L;
	
	private HubSpuPendingPic hubSpuPendingPic;
	
	private HubSpuPendingPicCriteria criteria;

}
