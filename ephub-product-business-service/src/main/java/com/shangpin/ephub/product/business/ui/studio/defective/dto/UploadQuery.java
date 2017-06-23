package com.shangpin.ephub.product.business.ui.studio.defective.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: UploadQuery</p>
 * <p>Description: 图片上传的请求参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月23日 上午10:41:29
 *
 */
@Getter
@Setter
public class UploadQuery {
	/**
	 * 扫码的码
	 */
	private String slotNoSpuId;
	/**
	 * 上传的文件的base64编码
	 */
	private List<String> urls;

}
