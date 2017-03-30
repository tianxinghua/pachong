package com.shangpin.ephub.client.consumer.picture.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:UploadPicDto.java </p>
 * <p>Description: 重试图片数据传输对象</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月3日 下午5:06:59
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetryPictureDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4975561245426069166L;
	/**
	 * 重试图片表主键列表
	 */
	private List<Long> ids;
}
