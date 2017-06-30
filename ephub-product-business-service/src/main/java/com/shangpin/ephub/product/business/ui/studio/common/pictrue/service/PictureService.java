package com.shangpin.ephub.product.business.ui.studio.common.pictrue.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.fdfs.dto.DeletePicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.DeletePicGateWay;
import com.shangpin.ephub.client.fdfs.gateway.UploadPicGateway;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto.UploadPic;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;
/**
 * <p>Title: PictureService</p>
 * <p>Description: 图片的一些服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月14日 上午11:16:16
 *
 */
@SuppressWarnings("restriction")
@Service
@Slf4j
public class PictureService {
	
	@Autowired
	private UploadPicGateway uploadPicGateway;
	@Autowired
	private DeletePicGateWay deletePicGateWay;
	
	public String uploadPic(UploadPic uploadPic){
		try {
			if(StringUtils.isNoneEmpty(uploadPic.getContent())){
                UploadPicDto uploadPicDto = new UploadPicDto();
                uploadPicDto.setBase64(uploadPic.getContent());
                uploadPicDto.setExtension(uploadPic.getExtension());
                return uploadPicGateway.upload(uploadPicDto);
            }
		} catch (Exception e) {
			log.error("上传图片异常："+e.getMessage(),e); 
		}
		return null;
	}

	/**
	 * 上传图片
	 * @param byteArray 上传的文件byte
	 * @param extension 扩展名
	 * @return fsdfs返回的图片链接
	 */
	public String uploadPic(byte[] byteArray, String extension){
		UploadPicDto uploadPicDto = new UploadPicDto();
		String base64 = new BASE64Encoder().encode(byteArray );
		uploadPicDto.setBase64(base64);
		uploadPicDto.setExtension(extension);
		return uploadPicGateway.upload(uploadPicDto);
	}
	/**
	 * 删除图片
	 * @param urls 要删除的图片链接
	 * @return
	 */
	public Map<String,Integer> deletePics(List<String> urls){
		DeletePicDto dto = new DeletePicDto();
		dto.setUrls(urls);
		return deletePicGateWay.delete(dto );
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
