package com.shangpin.ephub.data.mysql.picture.hub.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPic;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPicCriteria;

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
public class HubPicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5900601029453733848L;

	private HubPic hubPic;
	
	private HubPicCriteria criteria;
}
