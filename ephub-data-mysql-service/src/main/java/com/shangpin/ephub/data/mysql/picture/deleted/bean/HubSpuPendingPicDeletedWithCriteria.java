package com.shangpin.ephub.data.mysql.picture.deleted.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeleted;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeletedCriteria;

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
public class HubSpuPendingPicDeletedWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8098281033321763241L;
	
	private HubSpuPendingPicDeleted hubSpuPendingPic;
	
	private HubSpuPendingPicDeletedCriteria criteria;

}
