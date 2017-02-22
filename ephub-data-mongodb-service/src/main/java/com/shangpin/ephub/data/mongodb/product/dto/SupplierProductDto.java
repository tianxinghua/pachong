package com.shangpin.ephub.data.mongodb.product.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * <p>Title: SupplierProduct</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月22日 下午3:30:50
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SupplierProductDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2162871494223587295L;
	/**
	 * id
	 */
	@Id
	private String id;
	/**
	 * 商品数据
	 */
	private Map<String,Object> product; 
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
