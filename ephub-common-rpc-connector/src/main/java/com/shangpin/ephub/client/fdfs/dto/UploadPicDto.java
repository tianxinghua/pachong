package com.shangpin.ephub.client.fdfs.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:UploadPicDto.java </p>
 * <p>Description: 图片数据传输对象</p>
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
public class UploadPicDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4975561245426069166L;
	/**
	 * 图片BASE64编码之后的字符串
	 */
	private String base64;

}
