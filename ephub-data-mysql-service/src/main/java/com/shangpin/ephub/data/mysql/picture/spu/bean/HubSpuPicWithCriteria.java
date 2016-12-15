package com.shangpin.ephub.data.mysql.picture.spu.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPic;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPicCriteria;

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
public class HubSpuPicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3998232620688622090L;

	private HubSpuPic hubSpuPic;
	
	private HubSpuPicCriteria criteria;
}
