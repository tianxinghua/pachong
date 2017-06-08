package com.shangpin.ephub.client.data.mysql.hub.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HubFilterRequest {

	private String hubCategoryNo;
	private Byte filterType;
}