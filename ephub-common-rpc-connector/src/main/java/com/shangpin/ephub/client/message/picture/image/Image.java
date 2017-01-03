package com.shangpin.ephub.client.message.picture.image;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Image.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月1日 下午7:21:47
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7664818101179609333L;
	/**
	 * 图片绝对路径地址
	 */
	private String url;
}
