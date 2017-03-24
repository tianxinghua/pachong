package com.shangpin.picture.product.consumer.conf.stream.source.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:RetryPicture.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月18日 下午12:20:21
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetryPicture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8329268266608806631L;
	/**
	 * 图片表主键
	 */
	private Long spuPendingPicId;
}
