package com.shangpin.ephub.data.mysql.hub.filter.po;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class HubFilterResponse {
	private Long hubFilterId;
	private String hubGender;
	private String hubCategoryNo;
	private String hubBrandNo;
	private Byte filterType;
	private Date updateTime;
	private String memo;
	private String updateUser;
}
