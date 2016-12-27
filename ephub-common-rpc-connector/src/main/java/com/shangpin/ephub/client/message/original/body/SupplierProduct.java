package com.shangpin.ephub.client.message.original.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:SupplierProduct.java </p>
 * <p>Description: 供货商商品实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 上午11:50:52
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierProduct {
	/**
	 * 消息ID
	 */
	private String messageId;
	/**
	 * 消息发生的时间 Dateime
	 */
	private String messageDate;
	/**
	 * 供货商ID
	 */
	private String supplierId;
	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * 供货商名称
	 */
	private String supplierName;
	/** 请注意：该字段暂未使用，预留后期使用。
	 * JSON | ATELIER | CSV
	   JSON: 标准数据
       ATELIER: Atelier系统数据
       CSV: CSV商品数据
	 */
	private String messageType;
	
	private String data;
}
