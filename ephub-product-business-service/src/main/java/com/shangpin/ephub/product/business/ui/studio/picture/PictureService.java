package com.shangpin.ephub.product.business.ui.studio.picture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.UploadPicGateway;

import sun.misc.BASE64Encoder;
/**
 * <p>Title: PictureService</p>
 * <p>Description: 图片的一些服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月14日 上午11:16:16
 *
 */
@Service
public class PictureService {
	
	@Autowired
	private UploadPicGateway uploadPicGateway;

	/**
	 * 上传图片
	 * @param byteArray 上传的文件byte
	 * @param picId 图片的唯一标识
	 * @param extension 扩展名
	 * @return fsdfs返回的图片链接
	 */
	public String uploadPic(byte[] byteArray, Long picId, String extension){
		UploadPicDto uploadPicDto = new UploadPicDto();
		uploadPicDto.setRequestId(String.valueOf(picId));
		String base64 = new BASE64Encoder().encode(byteArray);
		uploadPicDto.setBase64(base64);
		uploadPicDto.setExtension(extension);
		return uploadPicGateway.upload(uploadPicDto);
	}
	
	/**
	 * 获取图片扩张名
	 * @param url 图片地址
	 * @return 扩张名
	 */
	public String getExtension(String url){
		if (StringUtils.isNotBlank(url)) {
			 String suffix = url.substring(url.lastIndexOf(".")+1);
			 if (suffix == null) {
				 return "jpg";
			 }
			 if (suffix != null && suffix.length() > 5) {
				return "jpg";
			}
			return suffix;
		} else {
			return null;
		}
	}
}
