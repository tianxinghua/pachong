package com.shangpin.ephub.product.business.ui.studio.common.pictrue.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto.UploadPic;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: PicUploadController</p>
 * <p>Description: 图片上传controller </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月23日 下午4:05:17
 *
 */
@RestController
@RequestMapping("/api/airstudio/pic")
@Slf4j
public class PicUploadController {
	
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping(value="/upload-pic",method = RequestMethod.POST)
	public HubResponse<?> upload(@RequestBody UploadPic uploadPic){
		List<String> list = pictureService.uploadPic(uploadPic);
		if(CollectionUtils.isNotEmpty(list)){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("上传失败");
		}
	}

	@RequestMapping(value="/upload",method = RequestMethod.POST)
	public HubResponse<?> upload(HttpServletRequest request){
		try {
			log.info("========开始上传图片==========="); 
			List<String> urls = new ArrayList<String>();
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart){
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
				Map<String, MultipartFile> maps = mreq.getFileMap();
				if(null != maps && maps.size()>0){
					for(MultipartFile file : maps.values()){
						String fileName = file.getOriginalFilename();
						log.info("上传图片："+fileName);
						String extension = pictureService.getExtension(fileName);
						String fdfsURL = pictureService.uploadPic(file.getBytes(), extension);
						log.info("上传成功："+fdfsURL);
						urls.add(fdfsURL);
					}
				}
			}else{
				log.error("This request is not Multipart!"); 
				return HubResponse.errorResp("This request is not Multipart!");
			}
			return HubResponse.successResp(urls);
		} catch (Exception e) {
			log.error("上传图片时异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("调用接口异常");
	}
}
