package com.shangpin.ephub.data.mysql.hub.filter.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HubFilterRequest {

	private String hubCategoryNo;
	private Byte filterType;
}
