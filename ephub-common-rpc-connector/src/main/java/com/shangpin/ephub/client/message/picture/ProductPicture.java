package com.shangpin.ephub.client.message.picture;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.message.picture.image.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:ProductPicture.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月1日 下午7:18:22
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPicture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8986325259692312708L;
	/**
	 * 供应商spu编号
	 */
	private String supplierSpuNo;
	
	private List<Image> images;
}
