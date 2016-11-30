package com.shangpin.message.order.mongodb.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.shangpin.message.conf.stream.source.order.message.SupplierOrderSync;
import com.shangpin.message.order.dto.SupplierOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:SupplierOrderEntity.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月29日 下午4:19:29
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document
public class SupplierOrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5639362569697758310L;
	/**
	 * 主键
	 */
	@Id
	private String id;
	/**
	 * 拆分之前的数据
	 */
	private SupplierOrderDTO beforeSplit;
	/**
	 * 拆分之后的数据
	 */
	private List<SupplierOrderSync> afterSplit;
	/**
	 * 是否发送成功
	 */
	private Boolean status;
}
