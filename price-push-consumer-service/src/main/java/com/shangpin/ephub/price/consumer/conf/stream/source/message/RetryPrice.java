package com.shangpin.ephub.price.consumer.conf.stream.source.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

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
public class RetryPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8329268266608806631L;
	/**
	 * 图片表主键
	 */
	private Long spuPendingPicId;
}
