package com.shangpin.ephub.client.product.business.hubpending.spu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by loyalty on 16/12/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingAuditDto {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3008587403829352646L;
	/**
     * 0 :信息待完善 1 : 待复核 2 :已处理(审核通过) 3：无法处理 4:过滤不处理
     */
    private Integer   auditStatus;

    private String startDate;

    private String endDate;
	/**
	 * 单个更新 还是按查询条件多个更新
	 * 按查询条件:true 单个:false
	 */
	private boolean isMulti;
	/**
	 * 复核人
	 */
	private String auditUser;
	
	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	@JsonProperty("auditStatus")
	public Integer getAuditStatus() {
		return auditStatus;
	}
	@JsonProperty("auditStatus")
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	@JsonProperty("startDate")
	public String getStartDate() {
		return startDate;
	}
	@JsonProperty("startDate")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@JsonProperty("endDate")
	public String getEndDate() {
		return endDate;
	}
	@JsonProperty("endDate")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@JsonProperty("isMulti")
	public boolean isMulti() {
		return isMulti;
	}
	@JsonProperty("isMulti")
	public void setMulti(boolean multi) {
		isMulti = multi;
	}
}
