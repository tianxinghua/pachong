package com.shangpin.ephub.client.message.picture.body;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.message.picture.ProductPicture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:SupplierProduct.java </p>
 * <p>Description: 供货商商品图片实体类</p>
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
public class SupplierPicture implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5267765072624963014L;
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
	 * 供货商名称
	 */
	private String supplierName;
	/**
	 * 供应商原始数据表主键id
	 */
	private Long supplierSpuId;
	
	private ProductPicture productPicture;
}
