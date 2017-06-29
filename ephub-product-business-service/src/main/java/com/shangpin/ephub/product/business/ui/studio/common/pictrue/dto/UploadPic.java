package com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: UploadPic</p>
 * <p>Description: 图片上传参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月29日 上午10:16:20
 *
 */
@Getter
@Setter
public class UploadPic {

	private String extension;
	private List<String> files;
}
